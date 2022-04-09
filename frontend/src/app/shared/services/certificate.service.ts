import {EventEmitter, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {NewCertificateDto} from "../../new-certificate/dtos/new-certificate.dto";
import {ExtensionSettingsDto} from "../../new-certificate/dtos/extension-settings.dto";
import {Certificate} from "../../certificate-overview/model/certificate.model";
import {saveAs as importedSaveAs} from "file-saver";

@Injectable({providedIn: "root"})
export class CertificateService {
  public certificatesChange: EventEmitter<Array<Certificate>> = new EventEmitter<Array<Certificate>>();

  constructor(private _http: HttpClient) {
  }

  getAll(): Observable<Array<any>>{
    return this._http.get<Array<any>>(environment.apiUrl + "/certificates/all");
  }

  getCertKeyUsages(): Observable<Array<any>>{
    return this._http.get<Array<any>>(environment.apiUrl + "/extensions/keyUsages");
  }

  getCertExtensions(): Observable<Array<any>>{
    return this._http.get<Array<any>>(environment.apiUrl + "/extensions/extensions");
  }

  getCertExtKeyUsages() {
    return this._http.get<Array<any>>(environment.apiUrl + "/extensions/extKeyUsages");
  }

  createdCertificate(certType: string, certData: any, extensions: any, keyUsage: any, extKeyUsage: any) {
    let extensionDto = new ExtensionSettingsDto(extensions, keyUsage, extKeyUsage);
    let newCertDto = new NewCertificateDto(certData.issuer.email, certData.endValidityDate, certData.organizationName, certData.organizationUnitName, certData.country, null, certType, extensionDto);

    if(certType == "ROOT"){
      return this._http.post(environment.apiUrl + "/certificates/createRootCertificate", newCertDto);
    }else if(certType == "INTERMEDIATE"){
      newCertDto.subjectEmail = certData.subject.email;
      newCertDto.issuerSerialNumber = certData.issuer.issuerSerialNumber;
      return this._http.post(environment.apiUrl + "/certificates/createIntermediateCertificate", newCertDto);
    }else{
      newCertDto.subjectEmail = certData.subject.email;
      newCertDto.issuerSerialNumber = certData.issuer.issuerSerialNumber;
      return this._http.post(environment.apiUrl + "/certificates/createEndEntityCertificate", newCertDto);
    }
  }

  revoke(serialNumber: number) {
    return this._http.put<boolean>(environment.apiUrl + "/certificates/revoke", serialNumber.toString());
  }

  validityCheck(serialNumber: number) {
    return this._http.get<boolean>(environment.apiUrl + "/certificates/validityCheck/" + serialNumber.toString());
  }

  download(serialNumber: number): Observable<Blob> {
    return this._http.get(environment.apiUrl + '/certificates/download/' + serialNumber, {responseType: 'blob'});
  }

  search(searchText: string) {
    return this._http.get<Array<Certificate>>(environment.apiUrl + "/certificates/search/" + searchText).subscribe((response) =>{
      this.certificatesChange.emit(response);
    });
  }

  filterByType(type: string) {
    return this._http.get<Array<Certificate>>(environment.apiUrl + "/certificates/filterByType/" + type).subscribe((response) => {
      this.certificatesChange.emit(response);
    });

  }
}
