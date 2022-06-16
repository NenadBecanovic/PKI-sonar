package bsep.pkiapp.controller;

import bsep.pkiapp.dto.ChangedPasswordDto;
import bsep.pkiapp.dto.ForgottenPasswordDto;
import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.security.exception.ResourceConflictException;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.TfaAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping(value = "getRole")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/getRole from JWT token");
        return ResponseEntity.ok(authenticationService.getRoleFromToken(token.split(" ")[1]));
    }

    @GetMapping(value = "getAuthorities")
    public ResponseEntity<List<String>> getAuthorities(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/getAuthorities from JWT token");
        return ResponseEntity.ok(authenticationService.getAuthoritiesFromToken(token.split(" ")[1]));
    }

    @GetMapping(value = "getUser")
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/getUser from JWT token");
        return ResponseEntity.ok(authenticationService.getUser(token.split(" ")[1]));
    }

    @GetMapping(value = "2fa")
    public ResponseEntity<Boolean> getUsing2fa(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/2fa is 2fa enabled");
        return ResponseEntity.ok(authenticationService.getUsing2fa(token.split(" ")[1]));
    }

    @GetMapping(value = "2fa/enable")
    public ResponseEntity<String> enable2fa(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/2fa/enable enable 2fa");
        return new ResponseEntity<>(authenticationService.enable2fa(token.split(" ")[1]), HttpStatus.OK);
    }

    @GetMapping(value = "2fa/disable")
    public ResponseEntity<String> disable2fa(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /auth/2fa/enable enable 2fa");
        authenticationService.disable2fa(token.split(" ")[1]);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String token,
                                                 @RequestBody ChangedPasswordDto passwordDto) {
        log.debug("POST request received - /auth/change-password with password data: {}", passwordDto);
        if (!authenticationService.areNewPasswordsMatching(passwordDto.getNewPassword(), passwordDto.getNewPasswordRetyped()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (!authenticationService.isNewPasswordValid(passwordDto.getNewPassword()) || !authenticationService.isPasswordValid(passwordDto.getOldPassword()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            boolean isSuccess = authenticationService.changePassword(token.split(" ")[1], passwordDto);
            if (isSuccess)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        if (!authenticationService.isEmailValid(authenticationRequest.getEmail()) || !authenticationService.isPasswordValid(authenticationRequest.getPassword()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            UserTokenState userTokenState = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(userTokenState);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/tfa-login")
    public ResponseEntity<UserTokenState> tfaLogin(@RequestBody TfaAuthenticationRequest authenticationRequest) {
        if (!authenticationService.isEmailValid(authenticationRequest.getEmail())
                || !authenticationService.isPasswordValid(authenticationRequest.getPassword())
                || !authenticationService.isCodeValid(authenticationRequest.getCode()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try{
            UserTokenState userTokenState = authenticationService.tfaLogin(authenticationRequest);
            return ResponseEntity.ok(userTokenState);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/passwordless-login")
    public ResponseEntity<UserTokenState> createAuthenticationTokenForPasswordlessLogin(
            @RequestParam(name = "token") String token) {
        try{
            UserTokenState userTokenState = authenticationService.loginPasswordless(token);
            return ResponseEntity.ok(userTokenState);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/login-link")
    public ResponseEntity<String> sendLoginLink(@RequestParam(name = "email") String email) {
        if (!authenticationService.isEmailValid(email))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        authenticationService.sendLoginLink(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        if (!authenticationService.isNewPasswordValid(userDto.getPassword())
                || !authenticationService.isEmailValid(userDto.getEmail())
                || !authenticationService.isNameValid(userDto.getName())
                || !authenticationService.isNameValid(userDto.getSurname()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            authenticationService.signUp(userDto);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (ResourceConflictException e){
            return new ResponseEntity<>(userDto, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<Boolean> confirmAccount(@RequestParam(name = "token") String token) {
        return new ResponseEntity<>(authenticationService.confirmAccount(token), HttpStatus.OK);
    }

    @GetMapping("/account-recovery")
    public ResponseEntity<String> recoverAccount(@RequestParam(name = "email") String email) {
        if (!authenticationService.isEmailValid(email))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        authenticationService.recoverAccount(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/token-check")
    public ResponseEntity<Boolean> isTokenValid(@RequestParam(name = "token") String token) {
        return new ResponseEntity<>(authenticationService.isTokenValid(token), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> recoverPassword(@RequestParam(name = "token") String token,
                                                  @RequestBody ForgottenPasswordDto passwordDto) {
        if (!authenticationService.areNewPasswordsMatching(passwordDto.getNewPassword(), passwordDto.getNewPasswordRetyped()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else if (!authenticationService.isNewPasswordValid(passwordDto.getNewPassword()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            authenticationService.setupNewPassword(token, passwordDto.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
