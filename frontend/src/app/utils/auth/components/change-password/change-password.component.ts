import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { NoSpaceValidator } from 'src/app/shared/validators/NoSpaceValidator';
import { ChangedPasswordDto } from '../../dtos/ChangedPasswordDto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  public CONTAINS_DIGIT_PATTERN:        string = "[a-zA-Z0-9!?#$@.*+_]*[0-9][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_UPPERCASE_PATTERN:    string = "[a-zA-Z0-9!?#$@.*+_]*[A-Z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_LOWERCASE_PATTERN:    string = "[a-zA-Z0-9!?#$@.*+_]*[a-z][a-zA-Z0-9!?#$@.*+_]*";
  public CONTAINS_SPECIAL_CHAR_PATTERN: string = "[a-zA-Z0-9!?#$@.*+_]*[!?#$@.*+_][a-zA-Z0-9!?#$@.*+_]*";
  
  public errorMessage: string = "";
  public hide = true;
  public oldPassword: FormControl = new FormControl('', [Validators.required]);
  public newPassword: FormControl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);
  public newPasswordRetyped: FormControl = new FormControl("", [Validators.required, Validators.pattern(this.CONTAINS_DIGIT_PATTERN), Validators.pattern(this.CONTAINS_LOWERCASE_PATTERN), Validators.pattern(this.CONTAINS_UPPERCASE_PATTERN), Validators.pattern(this.CONTAINS_SPECIAL_CHAR_PATTERN), Validators.minLength(8), Validators.maxLength(50), NoSpaceValidator.cannotContainSpace]);
  public form: FormGroup = new FormGroup({
    'oldPassword': this.oldPassword,
    'newPassword': this.newPassword,
    'newPasswordRetyped': this.newPasswordRetyped
  })

  constructor(private authService: AuthService, private router: Router, private _snackBar: MatSnackBar) { }

  ngOnInit(): void { }

  changePassword() {
    this.errorMessage = "";
    //alert(this.newPassword)
    //alert(this.newPasswordRetyped)
    if(this.newPassword.value != this.newPasswordRetyped.value){
      this.errorMessage = "Passwords not matching."
      return;
    }
    var dto: ChangedPasswordDto = new ChangedPasswordDto(this.oldPassword.value, this.newPassword.value, this.newPasswordRetyped.value)
    this.authService.changePassword(dto).subscribe(
      {
        next: (response) => {
          console.log("password changed");
          this._snackBar.open("Password changed.", "OK", {
            panelClass: ["black-snackbar"]
          });
          this.router.navigate(['/overview'])
        },
        error: (error: HttpErrorResponse) => {
          console.log("error: password not changed")
          this._snackBar.open("Error ocurred: password not changed.", "OK", {
            panelClass: ["black-snackbar"]
          });
        }
      })
  }

}
