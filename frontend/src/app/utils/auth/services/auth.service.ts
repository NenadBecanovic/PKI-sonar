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
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { TwoFactorAuthLoginComponent } from "../components/two-factor-auth-login/two-factor-auth-login.component";
import { LoginUser2faDto } from "../dtos/LoginUser2fa.dto";
import { MatSnackBar } from "@angular/material/snack-bar";

@Injectable({providedIn: 'root'})
export class AuthService{
  public logInUserChanged = new Subject<UserTokenStateDto>();
  helper = new JwtHelperService();
  //tfaModal: any | null = null;

  //headers = new HttpHeaders({'Content-Type' : 'application/json', 'Access-Control-Allow-Origin': 'https://localhost:8080'} );

  

  constructor(public matDialog: MatDialog, private _http: HttpClient, private router: Router) {
  }

  loginUser(loginUserDto: LoginUserDto){
    this._http.post<UserTokenStateDto>(environment.apiUrl + "/auth/login", {
      email: loginUserDto.email,
      password: loginUserDto.password
    }).subscribe(
      {
        next: (response) => {
          if(response.accessToken === "2fa"){
            this.open2faDialog(loginUserDto.email, loginUserDto.password);
          }
          else {
            localStorage.setItem('token', response.accessToken);
            console.log(response.accessToken)
            this.router.navigate(['/overview']).then();
            this.logInUserChanged.next(response);
          }
        },
        error: (error: HttpErrorResponse) => {
          this.logInUserChanged.error(error)
        }
      })
  }

  loginUser2fa(loginUserDto: LoginUser2faDto){
    let _snackBar: MatSnackBar
    this._http.post<UserTokenStateDto>(environment.apiUrl + "/auth/tfa-login", {
      email: loginUserDto.email,
      password: loginUserDto.password,
      code: loginUserDto.code
    }).subscribe(
      {
        next: (response) => {
          localStorage.setItem('token', response.accessToken);
          console.log(response.accessToken)
          this.router.navigate(['/overview']).then();
          this.logInUserChanged.next(response);
        },
        error: (error: HttpErrorResponse) => {
          //this.logInUserChanged.error(error);
          _snackBar.open("One-time token is not valid.", "OK", {
            panelClass: ["black-snackbar"]
          });
        }
      })
  }

  open2faDialog(emailp: string, passwordp: string): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.id = "2fa-code-modal";
    dialogConfig.height = "300px";
    dialogConfig.width = "28%";
    dialogConfig.data = { email: emailp, password: passwordp}
    const tfaModal = this.matDialog.open(TwoFactorAuthLoginComponent, dialogConfig);
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

  getUsing2fa(): Observable<boolean> {
    return this._http.get<boolean>(environment.apiUrl + "/auth/2fa");
  }

  enable2fa(){
    return this._http.get(environment.apiUrl + "/auth/2fa/enable", {responseType: "text"});
  }

  disable2fa(): Observable<string> {
    return this._http.get<string>(environment.apiUrl + "/auth/2fa/disable");
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
