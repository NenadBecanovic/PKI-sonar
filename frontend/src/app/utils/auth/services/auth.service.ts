import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import {LoginUserDto} from "../dtos/LoginUser.dto";
import {environment} from "../../../../environments/environment";
import {map, Observable, Subject} from "rxjs";
import {RegisterUsetDto} from "../../../shared/dto/RegisterUset.dto";
import {UserTokenStateDto} from "../dtos/UserTokenState.dto";
import {Router} from "@angular/router";

@Injectable({providedIn: 'root'})
export class AuthService{
  public logInUserChanged = new Subject<UserTokenStateDto>();

  constructor(private _http: HttpClient, private router: Router) {
  }

  loginUser(loginUserDto: LoginUserDto){
    this._http.post<UserTokenStateDto>(environment.apiUrl + "/auth/login", {
      email: loginUserDto.email,
      password: loginUserDto.password
    }).subscribe(
      {
        next: (response) => {
          localStorage.setItem('token', response.accessToken);
          console.log(response.accessToken)
          this.router.navigate(['/overview']).then();
          this.logInUserChanged.next(response);
        },
        error: (error: HttpErrorResponse) => {
          this.logInUserChanged.error(error)
        }
      })
  }

  register(user: RegisterUsetDto) {
    return this._http.post(environment.apiUrl + "/auth/register", {
      name: user.name,
      surname: user.surname,
      password: user.password,
      email: user.email
    });
  }

  getRole() {
    return this._http.get(environment.apiUrl + "/auth/getRole", {responseType: 'text'});
  }

  getPermissions(): Observable<string[]> {
    return this._http.get<string[]>(environment.apiUrl + "/auth/getAuthorities");
  }

  requestLoginWithEmail(email: string) : Observable<boolean> {
    return this._http.get<boolean>(environment.apiUrl + "/auth/login-link?email=" + email)
  }

  loginWithEmail(token: string | null) {
    this._http.get<UserTokenStateDto>(environment.apiUrl + "/auth/passwordless-login?token="+token).subscribe(
      {
        next: (response) => {
          localStorage.setItem('token', response.accessToken);
          console.log(response.accessToken)
          this.router.navigate(['/overview']).then();
          this.logInUserChanged.next(response);
        },
        error: (error: HttpErrorResponse) => {
          this.logInUserChanged.error(error)
        }
      })
  }


}
