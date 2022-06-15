import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-two-factor-auth-login',
  templateUrl: './two-factor-auth-login.component.html',
  styleUrls: ['./two-factor-auth-login.component.css']
})
export class TwoFactorAuthLoginComponent implements OnInit {
  public CONTAINS_DIGIT_PATTERN: string = "[0-9]*";

  public errorMessage: string = "";
  public hide = true;
  public code: FormControl = new FormControl('', [Validators.required, Validators.maxLength(6), Validators.minLength(6), Validators.pattern(this.CONTAINS_DIGIT_PATTERN)]);
  public form: FormGroup = new FormGroup({
    'code': this.code
  })

  constructor(private router: Router, private _snackBar: MatSnackBar) { }


  ngOnInit(): void {
  }

  send(){
    /*this.validationService.recoverAccount(this.secret.value).subscribe((response) => {
      console.log(response);
      this._snackBar.open("Success. Further steps will be sent to you via email if account exist.", "OK", {
        panelClass: ["black-snackbar"]
      });
      this.router.navigate(['/login'])
    })*/
  }
}
