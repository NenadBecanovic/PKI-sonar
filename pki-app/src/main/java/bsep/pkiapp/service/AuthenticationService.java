package bsep.pkiapp.service;

import bsep.pkiapp.dto.ChangedPasswordDto;
import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.ConfirmationToken;
import bsep.pkiapp.model.ConfirmationTokenType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.security.exception.ResourceConflictException;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private EmailService emailService;

    public UserTokenState login(JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        log.debug("User log in with data: {}", authenticationRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (!user.isActivated()) {
            log.debug("User {} is not activated!", user.getUsername());
            return null;
        }
        return getAuthentication(user);
    }

    public UserTokenState loginPasswordless(String token) throws AuthenticationException {
        log.debug("Passwordles login with token: {}", token);
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken.getTokenType().equals(ConfirmationTokenType.LOGIN_TOKEN)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    confirmationToken.getEmail(), null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.getByEmail(confirmationToken.getEmail());
            confirmationTokenService.deleteToken(confirmationToken);
            if (!user.isActivated()) {
                log.debug("User {} is not activated!", user.getUsername());
                return null;
            }
            return getAuthentication(user);
        }
        else
            return null;
    }

    public UserTokenState getAuthentication(User user) {
        return new UserTokenState(tokenUtils.generateToken(user.getEmail(), user.getRole().getAuthority(), user.getRole().getPermissionNames()), tokenUtils.getExpiredIn(), getRoles(user));
    }

    public List<String> getRoles(User user) {
        log.debug("Get roles for user {}", user.getUsername());
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public UserDto getUser(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        User user = userService.getByEmail(email);
        return new UserDto(user.getName(), user.getSurname(), email);
    }

    public void signUp(UserDto userDto) throws ResourceConflictException {
        log.debug("sign up new user with payload: {}", userDto);
        User user = new User(userDto);
        user.setRole(roleService.getById(1));
        if (userService.isEmailRegistered(user.getEmail()).equals(true)) {
            throw new ResourceConflictException("Email already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
            sendRegistrationEmail(user);
        }
    }

    public void sendLoginLink(String email) {
        log.debug("Send login link to email: {}", email);
        User user = userService.getByEmail(email);
        if (user != null) {
            ConfirmationToken token = confirmationTokenService.generateConfirmationToken(email, ConfirmationTokenType.LOGIN_TOKEN);
            emailService.sendLoginEmail(user, token.getToken());
            confirmationTokenService.encodeToken(token);
        }
    }

    private void sendRegistrationEmail(User user) {
        log.debug("Send registration email to user with email {}", user.getEmail());
        ConfirmationToken token = confirmationTokenService.generateConfirmationToken(user.getEmail(), ConfirmationTokenType.REGISTRATION_TOKEN);
        emailService.sendRegistrationEmail(user, token.getToken());
        confirmationTokenService.encodeToken(token);
    }

    public String getRoleFromToken(String token) {
        return tokenUtils.getRoleFromToken(token);
    }

    public List<String> getAuthoritiesFromToken(String token) {
        return roleService.getByName(tokenUtils.getRoleFromToken(token)).getPermissionNames();
    }

    public boolean confirmAccount(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken != null && confirmationToken.getTokenType().equals(ConfirmationTokenType.REGISTRATION_TOKEN)) {
            User user = userService.getByEmail(confirmationToken.getEmail());
            if(user == null)
                return false;
            user.setActivated(true);
            userService.saveUser(user);
            confirmationTokenService.deleteToken(confirmationToken);
            return true;
        }
        return false;
    }

    public void recoverAccount(String email) {
        log.debug("Recover account for user: {}", email);
        User user = userService.getByEmail(email);
        if (user != null) {
            ConfirmationToken token = confirmationTokenService.generateConfirmationToken(email, ConfirmationTokenType.RECOVERY_TOKEN);
            emailService.sendRecoveryEmail(user, token.getToken());
            confirmationTokenService.encodeToken(token);
        }
    }

    public boolean areNewPasswordsMatching(String newPassword, String newPasswordRetyped) {
        return newPassword.equals(newPasswordRetyped);
    }

    public boolean changePassword(String token, ChangedPasswordDto passwordDto){
        String email = tokenUtils.getEmailFromToken(token);
        log.debug("Change password for user: {}", email);
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, passwordDto.getOldPassword()));
            if(authentication.isAuthenticated()){
                User user = userService.getByEmail(email);
                user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
                userService.saveUser(user);
                return true;
            }
            return false;
        } catch (BadCredentialsException e){
            log.warn("Failed to change password for user: {}", email);
            return false;
        }

    }

    public void setupNewPassword(String token, String newPassword) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken.getTokenType().equals(ConfirmationTokenType.RECOVERY_TOKEN)) {
            User user = userService.getByEmail(confirmationToken.getEmail());
            log.debug("Set new password for user: {}", user.getUsername());
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.saveUser(user);
            confirmationTokenService.deleteToken(confirmationToken);
        }
    }

    public boolean isTokenValid(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken != null) {
            return !tokenUtils.isTokenExpired(token);
        }
        return false;
    }

    public boolean isEmailValid(String email){
        if(Pattern.matches("[a-z0-9.\\-_]{3,64}@([a-z0-9]+\\.){1,2}[a-z]+", email))
            return true;
        return false;
    }

    public boolean isPasswordValid(String password){
        if(Pattern.matches("[0-9A-Za-z!?#$@.*+_\\-]+", password))
            return true;
        return false;
    }

    public boolean isNameValid(String password){
        if(Pattern.matches("[A-Za-z ']+", password))
            return true;
        return false;
    }


    public boolean isNewPasswordValid(String password){
        boolean isValid = true;
        if (!Pattern.matches(".*[0-9].*", password)){
            System.out.println("\tdigit");
            isValid = false;
        }
        if (!Pattern.matches(".*[a-z].*", password)){
            System.out.println("\tlower");
            isValid = false;
        }
        if (!Pattern.matches(".*[A-Z].*", password)){
            System.out.println("\tupper");
            isValid = false;
        }
        if(!Pattern.matches(".*[!?#$@.*+_].*", password)){
            System.out.println("\tspecial");
            isValid = false;
        }
        if(!Pattern.matches("[0-9A-Za-z!?#$@.*+_]{8,50}", password)){
            System.out.println("\tgeneral");
            isValid = false;
        }
        return isValid;
    }
}
