package bsep.pkiapp.controller;

import bsep.pkiapp.dto.ForgottenPasswordDto;
import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.ConfirmationToken;
import bsep.pkiapp.security.exception.ResourceConflictException;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.service.AuthenticationService;
import bsep.pkiapp.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping(value = "getRole")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authenticationService.getRoleFromToken(token.split(" ")[1]));
    }

    @GetMapping(value = "getUser")
    public ResponseEntity<UserDto> getUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authenticationService.getUser(token.split(" ")[1]));
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        try{
            UserTokenState userTokenState = authenticationService.login(authenticationRequest);
            return ResponseEntity.ok(userTokenState);
        }catch (AuthenticationException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        try {
            authenticationService.signUp(userDto);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (ResourceConflictException e){
            return new ResponseEntity<>(userDto, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmAccount(@RequestParam(name = "token") String token) {
        authenticationService.confirmAccount(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/account-recovery")
    public ResponseEntity<String> recoverAccount(@RequestParam(name = "email") String email) {
        authenticationService.recoverAccount(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO: add password validation checks
    @PutMapping("/forgotten-password")
    public ResponseEntity<String> recoverPassword(@RequestParam(name = "token") String token,
                                                  @RequestBody ForgottenPasswordDto passwordDto) {
        if (!authenticationService.areNewPasswordsMatching(passwordDto.getNewPassword(), passwordDto.getNewPasswordRetyped()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
            authenticationService.setupNewPassword(token, passwordDto.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
