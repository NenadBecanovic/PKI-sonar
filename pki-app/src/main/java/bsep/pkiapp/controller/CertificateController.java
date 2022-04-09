package bsep.pkiapp.controller;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.service.CertificateService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequestMapping(value = "certificates")
@CrossOrigin
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllByUser(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(certificateService.getAllByUser(token.split(" ")[1]), HttpStatus.OK);
    }

    @PostMapping("/createRootCertificate")
    public ResponseEntity<?> createRootCertificate(@RequestBody NewCertificateDto certificateDto) {
        if (certificateDto.getCertificateType().equals("ROOT")) {
            certificateService.createCertificate(certificateDto);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createIntermediateCertificate")
    public ResponseEntity<?> createIntermediateCertificate(@RequestBody NewCertificateDto certificateDto) {
        if (certificateDto.getCertificateType().equals("INTERMEDIATE")) {
            certificateService.createCertificate(certificateDto);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createEndEntityCertificate")
    public ResponseEntity<?> createEndEntityCertificate(@RequestBody NewCertificateDto certificateDto) {
        if (certificateDto.getCertificateType().equals("END_ENTITY")) {
            certificateService.createCertificate(certificateDto);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download/{serialNumber}")
    @Transactional
    public ResponseEntity<?> downloadCertificate(@PathVariable String serialNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.set("Content-Disposition", "attachment; filename=\"certificate-" + serialNumber.toString() + ".crt\"");

        try {
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(certificateService.getCertificate(new BigInteger(serialNumber)));
        } catch (AuthorizationServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getRootCertificates")
    public ResponseEntity<?> getRootCertificates() {
        return new ResponseEntity<>(certificateService.getRootCertificates(), HttpStatus.OK);
    }

    @PutMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody String certSerialNumber) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/validityCheck/{certSerialNumber}")
    public ResponseEntity<?> validityCheck(@PathVariable String certSerialNumber) {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> search(@RequestHeader("Authorization") String token, @PathVariable String searchText) {
        return new ResponseEntity<>(certificateService.searchCertificates(token.split(" ")[1], searchText),
                HttpStatus.OK);
    }

    @GetMapping("/filterByType/{filter}")
    public ResponseEntity<?> filterByType(@RequestHeader("Authorization") String token, @PathVariable String filter) {
        return new ResponseEntity<>(certificateService.filterCertificates(token.split(" ")[1], filter), HttpStatus.OK);
    }

}
