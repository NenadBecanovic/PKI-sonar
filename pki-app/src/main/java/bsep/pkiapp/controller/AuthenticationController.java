package bsep.pkiapp.controller;

import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.security.exception.ResourceConflictException;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.service.AuthenticationService;
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
}
