import {ExtensionSettingsDto} from "./extension-settings.dto";

export class NewCertificateDto{
  public subjectEmail: string;
  public validityEndDate: Date;
  public organizationName: string;
  public organizationUnit: string;
  public country: string;
  public issuerSerialNumber: string;
  public certificateType: string;
  public extensionSettingsDto: ExtensionSettingsDto;


  constructor(subjectEmail: string, validityEndDate: Date, organizationName: string, organizationUnit: string, country: string, issuerSerialNumber: any, certificateType: string, extensionSettingsDto: ExtensionSettingsDto) {
    this.subjectEmail = subjectEmail;
    this.validityEndDate = validityEndDate;
    this.organizationName = organizationName;
    this.organizationUnit = organizationUnit;
    this.country = country;
    this.issuerSerialNumber = issuerSerialNumber;
    this.certificateType = certificateType;
    this.extensionSettingsDto = extensionSettingsDto;
  }
}
