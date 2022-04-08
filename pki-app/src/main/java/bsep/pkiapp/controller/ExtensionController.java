package bsep.pkiapp.controller;

import bsep.pkiapp.model.CertificateExtension;
import bsep.pkiapp.model.ExtendedKeyUsage;
import bsep.pkiapp.model.KeyUsage;
import bsep.pkiapp.service.ExtendedKeyUsageService;
import bsep.pkiapp.service.ExtensionService;
import bsep.pkiapp.service.KeyUsageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "extensions")
public class ExtensionController {

    @Autowired
    public ExtensionService extensionService;

    @Autowired
    public KeyUsageService keyUsageService;

    @Autowired
    public ExtendedKeyUsageService extendedKeyUsageService;

    @GetMapping(value = "extensions")
    public ResponseEntity<List<CertificateExtension>> getAllExtensions() {
        return ResponseEntity.ok(extensionService.getAll());
    }

    @GetMapping(value = "keyUsages")
    public ResponseEntity<List<KeyUsage>> getAllKeyUsages() {
        return ResponseEntity.ok(keyUsageService.getAll());
    }

    @GetMapping(value = "extKeyUsages")
    public ResponseEntity<List<ExtendedKeyUsage>> getAllExtendedKeyUsages() {
        return ResponseEntity.ok(extendedKeyUsageService.getAll());
    }

}
