package bsep.pkiapp.service;

import bsep.pkiapp.dto.ChangedPasswordDto;
import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.ConfirmationToken;
import bsep.pkiapp.model.ConfirmationTokenType;
import bsep.pkiapp.model.User;
import bsep.pkiapp.security.exception.ResourceConflictException;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.TfaAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.security.util.TokenUtils;
import de.taimos.totp.TOTP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
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
        log.debug("L U: {}", authenticationRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (!user.isActivated()) {
            log.debug("NA U: {}", user.getUsername());
            return null;
        }
        UserTokenState token = getAuthentication(user);
        if(user.isUsing2FA()){
            token.setAccessToken("2fa");
        }
        return token;
    }

    public UserTokenState tfaLogin(TfaAuthenticationRequest authenticationRequest) throws AuthenticationException {
        log.debug("2FAL U: {}", authenticationRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        if (!user.isActivated()) {
            log.debug("NA U: {}", user.getUsername());
            return null;
        }
        if (!user.isUsing2FA()) {
            log.debug("N2FA U: {}", user.getUsername());
            return null;
        }
        String secret = user.getSecret();
        String code = getTOTPCode(secret);
        if(authenticationRequest.getCode() == null || !(code.equals(authenticationRequest.getCode()))){
            log.debug("TOTP CNV U: {}", user.getUsername());
            return null;
        }
        return getAuthentication(user);
    }

    private String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }

    public UserTokenState loginPasswordless(String token) throws AuthenticationException {
        log.debug("PL T: {}", token);
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken.getTokenType().equals(ConfirmationTokenType.LOGIN_TOKEN)) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    confirmationToken.getEmail(), null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.getByEmail(confirmationToken.getEmail());
            confirmationTokenService.deleteToken(confirmationToken);
            if (!user.isActivated()) {
                log.debug("NA U: {}", user.getUsername());
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
        log.debug("GR U: {}", user.getUsername());
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    public boolean getUsing2fa(String token){
        String email = tokenUtils.getEmailFromToken(token);
        log.debug("CHECK 2FA U: {}", email);
        User user = userService.getByEmail(email);
        return user.isUsing2FA();
    }

    public String enable2fa(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        log.debug("TFAE U: {}",email);
        User user = userService.getByEmail(email);
        String secret = generateSecretKey();
        user.setSecret(secret);
        user.setUsing2FA(true);
        userService.saveUser(user);
        return getGoogleAuthenticatorBarCode(secret, user.getEmail(), "PKI");
    }

    private String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
        log.debug("GGABC");
        try {
            return "otpauth://totp/"
                    + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.debug("GGABC FAILED");
            throw new IllegalStateException(e);
        }
    }

    public void disable2fa(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        log.debug("TFAD U: {}", email);
        User user = userService.getByEmail(email);
        user.setUsing2FA(false);
        user.setSecret("");
        userService.saveUser(user);
    }

    public UserDto getUser(String token) {
        String email = tokenUtils.getEmailFromToken(token);
        User user = userService.getByEmail(email);
        return new UserDto(user.getName(), user.getSurname(), email);
    }

    public void signUp(UserDto userDto) throws ResourceConflictException {
        log.debug("SNU with payload: {}", userDto);
        User user = new User(userDto);
        user.setRole(roleService.getById(1));

        if (userService.isEmailRegistered(user.getEmail()).equals(true)) {
            log.debug("SNU FAILED UEAE U: {}", userDto.getEmail());
            throw new ResourceConflictException("Email already exists");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
            sendRegistrationEmail(user);
        }
    }

    private static String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public void sendLoginLink(String email) {
        log.debug("SLL to U: {}", email);
        User user = userService.getByEmail(email);
        if (user != null) {
            ConfirmationToken token = confirmationTokenService.generateConfirmationToken(email, ConfirmationTokenType.LOGIN_TOKEN);
            emailService.sendLoginEmail(user, token.getToken());
            confirmationTokenService.encodeToken(token);
        }
    }

    private void sendRegistrationEmail(User user) {
        log.debug("SRE to U: {}", user.getEmail());
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
        log.debug("AC");
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken != null && confirmationToken.getTokenType().equals(ConfirmationTokenType.REGISTRATION_TOKEN)) {
            User user = userService.getByEmail(confirmationToken.getEmail());
            if(user == null)
                return false;
            log.debug("CA U: {}", user.getEmail());
            user.setActivated(true);
            userService.saveUser(user);
            confirmationTokenService.deleteToken(confirmationToken);
            return true;
        }
        return false;
    }

    public void recoverAccount(String email) {
        log.debug("RA U: {}", email);
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
        log.debug("CP U: {}", email);
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
            log.warn("FAILED CP U: {}", email);
            return false;
        }

    }

    public void setupNewPassword(String token, String newPassword) {
        ConfirmationToken confirmationToken = confirmationTokenService.findByToken(token);
        if (confirmationToken.getTokenType().equals(ConfirmationTokenType.RECOVERY_TOKEN)) {
            User user = userService.getByEmail(confirmationToken.getEmail());
            log.debug("SNP U: {}", user.getUsername());
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

    public boolean isCodeValid(String code){
        if(Pattern.matches("[0-9]*", code))
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
