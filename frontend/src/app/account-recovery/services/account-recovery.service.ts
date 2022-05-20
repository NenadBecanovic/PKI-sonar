import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountRecoveryService {

  constructor(private _http: HttpClient) {
  }
  recoverAccount(email: string | null): Observable<any>  {
    return this._http.get<any>(environment.apiUrl + "/auth/account-recovery?email="+email);
  }
}
