import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import {LoginUserDto} from "../dtos/LoginUser.dto";
import {environment} from "../../../../environments/environment";
import {map, Subject} from "rxjs";
import {RegisterUsetDto} from "../dtos/RegisterUset.dto";
import {UserTokenStateDto} from "../dtos/UserTokenState.dto";

@Injectable({providedIn: 'root'})
export class AuthService{
  public logInUserChanged = new Subject<UserTokenStateDto>();

  constructor(private _http: HttpClient) {
  }

  loginUser(loginUserDto: LoginUserDto){
    this._http.post<UserTokenStateDto>(environment.apiUrl + "/auth/login", {
      email: loginUserDto.email,
      password: loginUserDto.password
    }).subscribe(
      {
        next: (response) => {
          console.log(response)
          localStorage.setItem('token', response.accessToken);
          this.logInUserChanged.next(response);
        },
        error: (error: HttpErrorResponse) => {
          this.logInUserChanged.error(error)
        }
      })
  }

  register(user: RegisterUsetDto) {
    // TODO: register user for USER and CA type?
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
}
