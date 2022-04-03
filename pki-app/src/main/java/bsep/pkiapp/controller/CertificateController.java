package bsep.pkiapp.controller;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

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
}
