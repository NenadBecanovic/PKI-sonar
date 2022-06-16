import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { LoginUserDto } from "../../dtos/LoginUser.dto";
import { Subscription } from "rxjs";
import { HttpErrorResponse } from "@angular/common/http";
import { MatSnackBar } from "@angular/material/snack-bar";
import { UserTokenStateDto } from "../../dtos/UserTokenState.dto";
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { TwoFactorAuthLoginComponent } from '../two-factor-auth-login/two-factor-auth-login.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  public errorMessage: string;
  public hide = true;
  public form: FormGroup;
  public email: FormControl;
  public password: FormControl;
  public loginUserChanged: Subscription;

  constructor(private router: Router, private authService: AuthService, private _snackBar: MatSnackBar) {
    this.errorMessage = "";
    this.loginUserChanged = this.authService.logInUserChanged.subscribe({
      next: (res) => {
      },
      error: (error: HttpErrorResponse) => {
        this.errorHandler(error);
      }
    })

    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.password = new FormControl('', [Validators.maxLength(64)]); //login -> ne cuva se u bazi i ne formira se upit na osnovu toga

    this.form = new FormGroup({
      'email': this.email,
      'password': this.password
    })
  }

  private errorHandler(error: HttpErrorResponse) {
    switch (error.status) {
      case 400:
        this.errorMessage = "Invalid credentials.";
        break;
      default:
        this.errorMessage = "Something went wrong. Please try again!";
    }
  }

  ngOnInit(): void {

  }

  ngOnDestroy() {
    this.loginUserChanged.unsubscribe();
  }

  onLogin() {
    this.errorMessage = "";
    if (this.form.valid) {
      this.authService.loginUser(new LoginUserDto(this.email.value, this.password.value));
    } else {
      console.log("Not valid")
    }
  }
}
