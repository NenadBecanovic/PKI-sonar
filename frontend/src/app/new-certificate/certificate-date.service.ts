import {Injectable} from "@angular/core";

@Injectable({providedIn: "root"})
export class CertificateDateService{
  private keyUsages: string[] = [
    'Digital Signature',
    'Non-repudiation',
    'Key encipherment',
    'Data encipherment',
    'Key agreement',
    'Certificate signing',
    'CRL signing',
    'Encipher only',
    'Decipher only'
  ];
  private extensions: string[] = [
    'Authority Information Access',
    'Authority Key Identifier',
    'Basic Constraints',
    'Certificate Policies',
    'CRL Distribution Points',
    'Issuer Alternative Name',
    'Name Constraints',
    'OCSP Nocheck',
    'Policy Constraints',
    'Policy Mappings',
    'Private Key Usage Period',
    'Subject Alternative Name',
    'Subject Directory Attributes',
    'Subject Key Identifier'
  ];


  getCertKeyUsages(){
    return this.keyUsages;
  }

  getCertExtenstions(){
    return this.extensions.slice();
  }

}
