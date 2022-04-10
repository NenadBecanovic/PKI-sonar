import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";
import {RegisterUsetDto} from "../../../../shared/dto/RegisterUset.dto";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public errorMessage: string;
  public hide = true;
  public loginUserChanged: Subscription;
  public form: FormGroup;
  public emailCtrl: FormControl;
  public passwordCtrl: FormControl;
  public nameCtrl: FormControl;
  public surnameCtrl: FormControl;

  public PASSWORD_PATTERN: string = "[a-zA-Z0-9]*[a-z]+[a-zA-Z0-9]*[A-Z]+[a-zA-Z0-9]*|[a-zA-Z0-9]*[A-Z]+[a-zA-Z0-9]*[a-z]+[a-zA-Z0-9]*";
  public PASSWORD_NUMBER_PATTERN: string = "[a-zA-Z0-9]*[0-9]+[a-zA-Z0-9]*|[a-zA-Z0-9]*[0-9]+[a-zA-Z0-9]*";

  constructor(private router: Router, private authService: AuthService, private _snackBar: MatSnackBar) {
    this.errorMessage = "";
    this.loginUserChanged = this.authService.logInUserChanged.subscribe({
      next: () => {
        this.router.navigate(['/overview']).then();
      },
      error: (error: HttpErrorResponse) => {
        this.errorHandler(error);
      }
    })

    this.nameCtrl = new FormControl("", Validators.required);
    this.surnameCtrl = new FormControl("", Validators.required);
    this.emailCtrl = new FormControl('', [Validators.required, Validators.email]);
    this.passwordCtrl = new FormControl("", [Validators.required, Validators.pattern(this.PASSWORD_PATTERN), Validators.pattern(this.PASSWORD_NUMBER_PATTERN)]);


    this.form = new FormGroup({
      'nameCtrl': this.nameCtrl,
      'surnameCtrl': this.surnameCtrl,
      'emailCtrl': this.emailCtrl,
      'passwordCtrl': this.passwordCtrl
    })
  }

  private errorHandler(error: HttpErrorResponse) {
    switch (error.status) {
      case 400:
        this.errorMessage = "Invalid email or password.";
        break;
      case 409:
        this.errorMessage = "Email already exists.";
        break;
      default:
        this.errorMessage = "Something went wrong. Please try again!";
    }
  }

  ngOnInit(): void {

  }

  onRegister() {
    if(this.form.valid){
      let user = new RegisterUsetDto(this.nameCtrl.value, this.surnameCtrl.value,
        this.emailCtrl.value, this.passwordCtrl.value);

      this.authService.register(user).subscribe({
        next: (res) =>{
          this.router.navigate(['login']).then();
        },
      error:(error: HttpErrorResponse)=>{
          this.errorHandler(error);
      }});
    }
  }


}


