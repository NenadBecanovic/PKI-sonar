import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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

  public errorMessage: string = "";
  public hide = true;
  public newPassword: FormControl = new FormControl('', [Validators.required]);
  public newPasswordRetyped: FormControl = new FormControl();
  public form: FormGroup = new FormGroup({
    'newPassword': this.newPassword,
    'newPasswordRetyped': this.newPasswordRetyped
  })

  constructor(private _route: ActivatedRoute, private recoveryService: PasswordResetService, private router: Router) { }

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
    if (this.form.valid) {
      this.recoveryService.resetPassword(this.token, new ForgottenPasswordDto(this.newPassword.value, this.newPasswordRetyped.value)).subscribe((response) => {
        this.isSuccess = response;
        console.log(this.isSuccess)

        this.router.navigate(['/login'])
      });
    } else {
      console.log("Not valid")
    }
  }

}
