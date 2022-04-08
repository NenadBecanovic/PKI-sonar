import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {RegisterUsetDto} from "../../shared/dto/RegisterUset.dto";
import {IssuerDto} from "../dtos/issuer.dto";

@Injectable({providedIn: "root"})
export class UserService {

  constructor(private _http: HttpClient) {
  }


  getIssuers(certType: String) {
    return this._http.get<Array<IssuerDto>>(environment.apiUrl + "/users/getIssuersByCertificateType/" + certType);
  }

  getSubjects() {
    return this._http.get<Array<RegisterUsetDto>>(environment.apiUrl + "/users/getSubjects");

  }
}
