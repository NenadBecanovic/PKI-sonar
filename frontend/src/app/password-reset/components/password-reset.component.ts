import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { NoSpaceValidator } from 'src/app/shared/validators/NoSpaceValidator';
import { ForgottenPasswordDto } from '../dtos/ForgottenPassword.dto';
import { PasswordResetService } from '../services/password-reset.service';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css']
})
export class PasswordResetComponent implements OnInit {
  public token: string | null = "";
  public isTokenValid: boolean = true;
  public isSuccess: boolean = false;

  
  public CONTAINS_DIGIT_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[0-9][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_UPPERCASE_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[A-Z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_LOWERCASE_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[a-z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_SPECIAL_CHAR_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[!?#$@.*+_][a-zA-Z0-9!?#$@.*+_]*";
  
  public errorMessage: string = "";
  public hide = true;
  public newPassword: FormControl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);
  public newPasswordRetyped: FormControl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);
  public form: FormGroup = new FormGroup({
    'newPassword': this.newPassword,
    'newPasswordRetyped': this.newPasswordRetyped
  })

  constructor(private _route: ActivatedRoute, private recoveryService: PasswordResetService, private router: Router, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.token = this._route.snapshot.paramMap.get('token');
    if (this.token === null) {
      this.token = ""
    }
    this.recoveryService.checkToken(this.token).subscribe((response) => {
      this.isTokenValid = response;
    })
  }

  resetPassword() {
    this.errorMessage = "";
    if(this.newPassword.value != this.newPasswordRetyped.value){
      this.errorMessage = "Passwords not matching."
      return;
    }
    if (this.form.valid) {
      this.recoveryService.resetPassword(this.token, new ForgottenPasswordDto(this.newPassword.value, this.newPasswordRetyped.value)).subscribe((response) => {
        this.isSuccess = response;
        console.log(this.isSuccess)
        
        this._snackBar.open("Password changed successfully.", "OK", {
          panelClass: ["black-snackbar"]
        });
        this.router.navigate(['/login'])
      });
    } else {
      console.log("Not valid")
    }
  }

}
