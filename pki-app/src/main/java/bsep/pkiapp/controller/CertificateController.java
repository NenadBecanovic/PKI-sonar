package bsep.pkiapp.controller;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (certificateDto.certificateType.equals("ROOT")) {
            certificateService.createCertificate(certificateDto);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createIntermediateCertificate")
    public ResponseEntity<?> createIntermediateCertificate(@RequestBody NewCertificateDto certificateDto) {
        if (certificateDto.certificateType.equals("INTERMEDIATE")) {
            certificateService.createCertificate(certificateDto);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/createEndEntityCertificate")
    public ResponseEntity<?> createEndEntityCertificate(@RequestBody NewCertificateDto certificateDto) {
        if (certificateDto.certificateType.equals("END_ENTITY")) {
            certificateService.createCertificate(certificateDto);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getRootCertificates")
    public ResponseEntity<?> getRootCertificates() {
        return new ResponseEntity<>(certificateService.getRootCertificates(),HttpStatus.OK);
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
        return new ResponseEntity<>(certificateService.searchCertificates(token.split(" ")[1], searchText), HttpStatus.OK);
    }

    @GetMapping("/filterByType/{filter}")
    public ResponseEntity<?> filterByType(@RequestHeader("Authorization") String token, @PathVariable String filter) {
        return new ResponseEntity<>(certificateService.filterCertificates(token.split(" ")[1], filter), HttpStatus.OK);
    }

}
