package bsep.pkiapp.controller;

import bsep.pkiapp.dto.NewCertificateDto;
import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.service.CertificateService;

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
    
    @GetMapping("/download/{serialNumber}")
	public ResponseEntity<?> downloadCertificate(@PathVariable Long serialNumber)
	{
		HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-type", MediaType.APPLICATION_OCTET_STREAM_VALUE);
	    headers.set("Content-Disposition","attachment; filename=\"certificate-" + serialNumber.toString() + ".crt\"");
	    
	    try {
			return ResponseEntity.status(HttpStatus.OK).headers(headers).body(certificateService.getCertificate(serialNumber));
		} catch (AuthorizationServiceException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
	}
    
    @GetMapping("/user/{id}")
    ResponseEntity<List<ArrayList<CertificateChain>>> getAllForUser(@PathVariable Integer id)
    {
		 List<ArrayList<CertificateChain>> certificateChain = certificateService.findAllForUser(id);
        return certificateChain == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                ResponseEntity.ok(certificateChain);
    }
    
	@GetMapping("/all")
	@CrossOrigin
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ArrayList<CertificateChain>>> getAll()
	{

		List<ArrayList<CertificateChain>> certificateChain = certificateService.findAllForAdmin();
		return certificateChain == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                ResponseEntity.ok(certificateChain);
	}
}
