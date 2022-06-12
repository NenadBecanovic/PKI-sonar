package bsep.pkiapp.controller;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.exception.BadRequestException;
import bsep.pkiapp.service.CertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.math.BigInteger;

@RestController
@RequestMapping(value = "certificates")
@CrossOrigin
@Slf4j
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllByUser(@RequestHeader("Authorization") String token) {
        log.debug("GET request received - /certificates/all");
        return new ResponseEntity<>(certificateService.getAllByUser(token.split(" ")[1]), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('create_root_certificate')")
    @PostMapping("/createRootCertificate")
    public ResponseEntity<?> createRootCertificate(@RequestBody NewCertificateDto certificateDto) {
        log.debug("POST request received - /certificates/createRootCertificate. Payload: {}", certificateDto);
        if (!certificateService.isNameValid(certificateDto.getOrganizationName())
                || !certificateService.isNameValid(certificateDto.getCountry())
                || !certificateService.isEmailValid(certificateDto.getSubjectEmail()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {

            if (certificateDto.getCertificateType().equals("ROOT")) {
                certificateService.createCertificate(certificateDto);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (BadRequestException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('create_inter_certificate')")
    @PostMapping("/createIntermediateCertificate")
    public ResponseEntity<?> createIntermediateCertificate(@RequestBody NewCertificateDto certificateDto) {
        log.debug("POST request received - /certificates/createIntermediateCertificate. Payload: {}", certificateDto);
        if (!certificateService.isNameValid(certificateDto.getOrganizationUnit())
                || !certificateService.isSerialNumberValid(certificateDto.getIssuerSerialNumber())
                || !certificateService.isEmailValid(certificateDto.getSubjectEmail()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            if (certificateDto.getCertificateType().equals("INTERMEDIATE")) {
                certificateService.createCertificate(certificateDto);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (
                BadRequestException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('create_ee_certificate')")
    @PostMapping("/createEndEntityCertificate")
    public ResponseEntity<?> createEndEntityCertificate(@RequestBody NewCertificateDto certificateDto) {
        log.debug("POST request received - /certificates/createEndEntityCertificate. Payload: {}", certificateDto);
        if (!certificateService.isSerialNumberValid(certificateDto.getIssuerSerialNumber())
                || !certificateService.isEmailValid(certificateDto.getSubjectEmail()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            if (certificateDto.getCertificateType().equals("END_ENTITY")) {
                certificateService.createCertificate(certificateDto);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (
                BadRequestException e) {
            System.out.println(e);
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/download/{serialNumber}")
    @Transactional
    public ResponseEntity<?> downloadCertificate(@PathVariable String serialNumber) {
        log.debug("GET request received - /certificates/download/{}", serialNumber);
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

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/getRootCertificates")
    public ResponseEntity<?> getRootCertificates() {
        log.debug("GET request received - /certificates/getRootCertificates");
        return new ResponseEntity<>(certificateService.getRootCertificates(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('revoke_certificate')")
    @PutMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody String certSerialNumber) {
        log.debug("PUT request received - /certificates/revoke. Payload: {}", certSerialNumber);
        certificateService.revokeCertificate(certSerialNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/validityCheck/{certSerialNumber}")
    public ResponseEntity<?> validityCheck(@PathVariable String certSerialNumber) {
        log.debug("GET request received - /certificates/validity/{}", certSerialNumber);
        if (!certificateService.isSerialNumberValid(certSerialNumber))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(certificateService.isCertificateValid(certSerialNumber), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/search/{searchText}")
    public ResponseEntity<?> search(@RequestHeader("Authorization") String token, @PathVariable String searchText) {
        log.debug("GET request received - /certificates/search/{}", searchText);
        if (!certificateService.isSearchTextValid(searchText))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(certificateService.searchCertificates(token.split(" ")[1], searchText),
                HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('read_certificate')")
    @GetMapping("/filterByType/{filter}")
    public ResponseEntity<?> filterByType(@RequestHeader("Authorization") String token, @PathVariable String filter) {
        log.debug("GET request received - /certificates/filterByType/{}", filter);
        return new ResponseEntity<>(certificateService.filterCertificates(token.split(" ")[1], filter), HttpStatus.OK);
    }

}
