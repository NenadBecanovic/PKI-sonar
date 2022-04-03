package bsep.pkiapp.controller;

import bsep.pkiapp.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;
}
