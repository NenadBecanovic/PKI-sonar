import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import {LoginUserDto} from "../dtos/LoginUser.dto";
import {environment} from "../../../../environments/environment";
import {map, Observable, Subject} from "rxjs";
import {RegisterUsetDto} from "../../../shared/dto/RegisterUset.dto";
import {UserTokenStateDto} from "../dtos/UserTokenState.dto";
import {Router} from "@angular/router";
import { ChangedPasswordDto } from "../dtos/ChangedPasswordDto";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({providedIn: 'root'})
export class AuthService{
  public logInUserChanged = new Subject<UserTokenStateDto>();
  helper = new JwtHelperService();

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
    if(localStorage.getItem('token') && this.helper.isTokenExpired(localStorage.getItem('token')!)){
      this.router.navigate(['/']).then()
      localStorage.clear()
      return new Observable<string>()
    }else{
      return this._http.get(environment.apiUrl + "/auth/getRole", {responseType: 'text'});
    }
  }

  getPermissions(): Observable<string[]> {
    return this._http.get<string[]>(environment.apiUrl + "/auth/getAuthorities");
  }

  changePassword(dto: ChangedPasswordDto)  {
    return this._http.post(environment.apiUrl + "/auth/change-password", dto);
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
