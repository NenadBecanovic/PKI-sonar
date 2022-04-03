package bsep.pkiapp.controller;

import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.security.util.JwtAuthenticationRequest;
import bsep.pkiapp.security.util.UserTokenState;
import bsep.pkiapp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) {
        UserTokenState userTokenState = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(userTokenState);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> addClient(@RequestBody UserDto userDto) {
        authenticationService.signUp(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}
