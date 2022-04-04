import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {LoginUserDto} from "../../dtos/LoginUser.dto";
import {Subscription} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserTokenStateDto} from "../../dtos/UserTokenState.dto";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public errorMessage: string;
  public hide = true;
  public form: FormGroup;
  public email: FormControl;
  public password: FormControl;
  public loginUserChanged: Subscription;

  constructor(private router: Router, private authService: AuthService, private _snackBar: MatSnackBar) {
    this.errorMessage = "";
    this.loginUserChanged = this.authService.logInUserChanged.subscribe({
      next:() =>{
        this.router.navigate(['/overview']).then();
        },
      error: (error: HttpErrorResponse) => {
        this.errorHandler(error);
    }
  })

    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.password = new FormControl();

    this.form = new FormGroup({
      'email': this.email,
      'password': this.password
    })
  }

  private errorHandler(error: HttpErrorResponse) {
    switch (error.status) {
      case 400:
        this.errorMessage = "Invalid email or password.";
        break;
      default:
        this.errorMessage = "Something went wrong. Please try again!";
    }
  }

  ngOnInit(): void {

  }

  onLogin() {
    if (this.form.valid) {
      console.log(this.form.value)
      // localStorage.setItem('role', 'admin');
      // this.router.navigate(['/overview']).then();

      this.authService.loginUser(new LoginUserDto(this.email.value, this.password.value));

    } else {
      console.log("Not valid")
    }
  }
}
