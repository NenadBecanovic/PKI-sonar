import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ForgottenPasswordDto } from '../dtos/ForgottenPassword.dto';

@Injectable({
  providedIn: 'root'
})
export class AccountRecoveryService {

  constructor(private _http: HttpClient, private router: Router) {
  }

  checkToken(token: string): Observable<boolean> {
    return this._http.get<boolean>(environment.apiUrl + "/auth/token-check?token="+token);
  }
  
  resetPassword(token: string | null, dto: ForgottenPasswordDto) : Observable<any>  {
    return this._http.post<any>(environment.apiUrl + "/auth/reset-password?token="+token, dto);
  }
}
