package bsep.pkiapp.dto;

import bsep.pkiapp.model.CertificateChain;
import bsep.pkiapp.model.User;

public class IssuerDto {
    public String displayName;
    public String email;
    public Long issuerSerialNumber;

    public IssuerDto(String displayName, String email, Long issuerSerialNumber) {
        this.displayName = displayName;
        this.email = email;
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public IssuerDto(CertificateChain cert) {
        this.displayName = cert.getCommonName();
        this.email = cert.getUser().getEmail();
        this.issuerSerialNumber = cert.getSerialNumber();
    }

    public IssuerDto(User user) {
        this.displayName = user.getName() + " " + user.getSurname();
        this.email = user.getEmail();
        this.issuerSerialNumber = null;
    }
}
