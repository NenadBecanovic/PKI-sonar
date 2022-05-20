import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AccountValidationService {
  
  constructor(private _http: HttpClient, private router: Router) {
  }

  checkToken(token: string): Observable<boolean> {
    return this._http.get<boolean>(environment.apiUrl + "/auth/token-check?token="+token);
  }
  
  validateAccount(token: string | null): Observable<boolean>  {
    return this._http.get<boolean>(environment.apiUrl + "/auth/confirm-account?token="+token);
  }
}
