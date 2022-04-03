package bsep.pkiapp.dto;

import java.util.Date;

public class NewCertificateDto {

    public Integer idSubject;

    public Date validityEndDate;

    public String organizationName;

    public String organizationUnit;

    public String country;

    public String issuerSerialNumber;

    public String certificateType;

    public NewCertificateDto() {}

    public NewCertificateDto(Integer idSubject, Date validityEndDate, String organizationName,
                             String organizationUnit, String country, String serialNumber, String certificateType) {
        this.idSubject = idSubject;
        this.validityEndDate = validityEndDate;
        this.organizationName = organizationName;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.issuerSerialNumber = serialNumber;
        this.certificateType = certificateType;
    }

    public Integer getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(Integer idSubject) {
        this.idSubject = idSubject;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public void setValidityEndDate(Date validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
}
