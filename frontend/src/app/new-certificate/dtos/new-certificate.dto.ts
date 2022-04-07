import {ExtensionSettingsDto} from "./extension-settings.dto";

export class NewCertificateDto{
  public idSubject: number;
  public validityEndDate: Date;
  public organizationName: string;
  public organizationUnit: string;
  public country: string;
  public issuerSerialNumber: string;
  public certificateType: string;
  public extensionSettingsDto: ExtensionSettingsDto;


  constructor(idSubject: number, validityEndDate: Date, organizationName: string, organizationUnit: string, country: string, issuerSerialNumber: string, certificateType: string, extensionSettingsDto: ExtensionSettingsDto) {
    this.idSubject = idSubject;
    this.validityEndDate = validityEndDate;
    this.organizationName = organizationName;
    this.organizationUnit = organizationUnit;
    this.country = country;
    this.issuerSerialNumber = issuerSerialNumber;
    this.certificateType = certificateType;
    this.extensionSettingsDto = extensionSettingsDto;
  }
}
