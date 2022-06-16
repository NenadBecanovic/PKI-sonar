package bsep.pkiapp.controller;

import bsep.pkiapp.dto.UserDto;
import bsep.pkiapp.model.User;
import bsep.pkiapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "users")
@Slf4j
public class UserController {

    @Autowired
    public UserService userService;

    @GetMapping(value = "getIssuersByCertificateType/{certType}")
    public ResponseEntity<?> getIssuersByCertificateType(@RequestHeader("Authorization") String token,
                                                                     @PathVariable String certType) {
        log.debug("GET RR - UGICT T: {}", certType);
        return ResponseEntity.ok(userService.getByRole(token.split(" ")[1], certType));
    }

    @GetMapping(value = "getSubjects")
    public ResponseEntity<?> getSubjects() {
        log.debug("GET RR - UGS");
        return ResponseEntity.ok(userService.getAllSubjects());
    }

}
