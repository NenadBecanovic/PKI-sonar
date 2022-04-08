package bsep.pkiapp.dto;

import bsep.pkiapp.model.CertificateChain;

import java.util.Date;

public class CertificateDto {
    public String subject;
    public String issuer;
    public Date startDate;
    public Date endDate;
    public String type;
    public Long serialNumber;

    public CertificateDto(String subject, String issuer, Date startDate, Date endDate) {
        this.subject = subject;
        this.issuer = issuer;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public CertificateDto(CertificateChain cert) {
        this.subject = cert.getCommonName();
        this.issuer =
                cert.getUser().getName() + " " + cert.getUser().getSurname() + " (" + cert.getUser().getEmail() + ")";
        this.startDate = cert.getDateFrom();
        this.endDate = cert.getDateTo();
        this.type = cert.getCertificateType().name();
        this.serialNumber = cert.getSerialNumber();
    }
}
