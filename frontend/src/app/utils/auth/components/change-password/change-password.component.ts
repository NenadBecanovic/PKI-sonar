import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ChangedPasswordDto } from '../../dtos/ChangedPasswordDto';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  public errorMessage: string = "";
  public hide = true;
  public oldPassword: FormControl = new FormControl('', [Validators.required]);
  public newPassword: FormControl = new FormControl('', [Validators.required]);
  public newPasswordRetyped: FormControl = new FormControl('', [Validators.required]);
  public form: FormGroup = new FormGroup({
    'oldPassword': this.oldPassword,
    'newPassword': this.newPassword,
    'newPasswordRetyped': this.newPasswordRetyped
  })

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void { }

  changePassword() {
    this.errorMessage = "";
    var dto: ChangedPasswordDto = new ChangedPasswordDto(this.oldPassword.value, this.newPassword.value, this.newPasswordRetyped.value)
    this.authService.changePassword(dto).subscribe(
      {
        next: (response) => {
          console.log("password changed")
          this.router.navigate(['/overview'])
        },
        error: (error: HttpErrorResponse) => {
          console.log("error: password not changed")
        }
      })
  }

}
