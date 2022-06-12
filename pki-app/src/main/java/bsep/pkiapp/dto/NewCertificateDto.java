package bsep.pkiapp.dto;

import lombok.ToString;

import java.util.Date;

@ToString
public class NewCertificateDto {

    private String subjectEmail;

    private Date validityEndDate;

    private String organizationName;

    private String organizationUnit;

    private String country;

    private String issuerSerialNumber;

    private String certificateType;

    private ExtensionSettingsDto extensionSettingsDto;

    public NewCertificateDto() {}

    public NewCertificateDto(String subjectEmail, Date validityEndDate, String organizationName,
                             String organizationUnit, String country, String serialNumber, String certificateType) {
        this.subjectEmail = subjectEmail;
        this.validityEndDate = validityEndDate;
        this.organizationName = organizationName;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.issuerSerialNumber = serialNumber;
        this.certificateType = certificateType;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public Date getValidityEndDate() {
        return validityEndDate;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public ExtensionSettingsDto getExtensionSettingsDto() {
        return extensionSettingsDto;
    }
}
