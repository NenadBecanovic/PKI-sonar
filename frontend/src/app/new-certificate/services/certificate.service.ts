import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs";
import {NewCertificateDto} from "../dtos/new-certificate.dto";
import {ExtensionSettingsDto} from "../dtos/extension-settings.dto";

@Injectable({providedIn: "root"})
export class CertificateService {

  constructor(private _http: HttpClient) {
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
    console.log(certType)
    console.log(certData)
    console.log(extensions)
    console.log(keyUsage)
    console.log(extKeyUsage)

    let extensionDto = new ExtensionSettingsDto(extensions, keyUsage, extKeyUsage);
    let newCertDto = new NewCertificateDto(certData.issuer.email, certData.endValidityDate, certData.organizationName, certData.organizationUnitName, certData.country, null, certType, extensionDto);

    console.log(newCertDto);

    if(certType == "ROOT"){
      return this._http.post(environment.apiUrl + "/certificates/createRootCertificate", newCertDto);
    }else if(certType == "CA"){
      return this._http.get(environment.apiUrl + "/certificates/createIntermediateCertificate");
    }else{
      return this._http.get(environment.apiUrl + "/certificates/createEndEntityCertificate");
    }
  }
}
