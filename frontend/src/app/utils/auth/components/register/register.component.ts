import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators } from "@angular/forms";
import { Subscription } from "rxjs";
import { Router } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { HttpErrorResponse } from "@angular/common/http";
import { RegisterUsetDto } from "../../../../shared/dto/RegisterUset.dto";
import { NoSpaceValidator } from 'src/app/shared/validators/NoSpaceValidator';
import ConfirmedValidator from "../../validators/confirmed.validator";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public errorMessage: string;
  public hide = true;
  public confirmHide = true;
  public loginUserChanged: Subscription;
  public form: FormGroup;
  public emailCtrl: FormControl;
  public passwordCtrl: FormControl;
  public confirmPasswordCtrl: FormControl;
  public nameCtrl: FormControl;
  public surnameCtrl: FormControl;

  public PERSONAL_NAME_PATTERN: string = "^[a-zA-Z0-9' ]+$";
  public CONTAINS_DIGIT_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[0-9][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_UPPERCASE_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[A-Z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_LOWERCASE_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[a-z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_SPECIAL_CHAR_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[!?#$@.*+_][a-zA-Z0-9!?#$@.*+_]*";


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

    this.nameCtrl = new FormControl("", [Validators.required, Validators.maxLength(64), Validators.pattern(this.PERSONAL_NAME_PATTERN)]);
    this.surnameCtrl = new FormControl("", [Validators.required, Validators.maxLength(64), Validators.pattern(this.PERSONAL_NAME_PATTERN)]);
    this.emailCtrl = new FormControl('', [Validators.required, Validators.email]);
    this.passwordCtrl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);
    this.confirmPasswordCtrl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);


    this.form = new FormGroup({
      'nameCtrl': this.nameCtrl,
      'surnameCtrl': this.surnameCtrl,
      'emailCtrl': this.emailCtrl,
      'passwordCtrl': this.passwordCtrl,
      'confirmPasswordCtrl': this.confirmPasswordCtrl
    },
      {
        validators: [ConfirmedValidator.match('passwordCtrl', 'confirmPasswordCtrl')]
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
    if (this.form.valid) {
      let user = new RegisterUsetDto(this.nameCtrl.value, this.surnameCtrl.value,
        this.emailCtrl.value, this.passwordCtrl.value);

      this.authService.register(user).subscribe({
        next: (res) => {
          this.router.navigate(['login']).then();

          this._snackBar.open("Success. Please check your email for further steps.", "OK", {
            panelClass: ["black-snackbar"]
          });
        },
        error: (error: HttpErrorResponse) => {

          this._snackBar.open("Error occured.", "OK", {
            panelClass: ["black-snackbar"]
          });
          this.errorHandler(error);
        }
      });
    }
  }


}


