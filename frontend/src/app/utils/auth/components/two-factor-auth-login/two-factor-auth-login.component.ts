import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import {ViewEncapsulation} from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from '../../services/auth.service';
import { LoginUser2faDto } from '../../dtos/LoginUser2fa.dto';

export interface DialogData {
  email: string;
  password: string;
}

@Component({
  selector: 'app-two-factor-auth-login',
  templateUrl: './two-factor-auth-login.component.html',
  styleUrls: ['./two-factor-auth-login.component.css'], 
  encapsulation: ViewEncapsulation.None
})
export class TwoFactorAuthLoginComponent implements OnInit {
  public CONTAINS_DIGIT_PATTERN: string = "[0-9]*";

  public errorMessage: string = "";
  public hide = true;
  public code: FormControl = new FormControl('', [Validators.required, Validators.maxLength(6), Validators.minLength(6), Validators.pattern(this.CONTAINS_DIGIT_PATTERN)]);
  public form: FormGroup = new FormGroup({
    'code': this.code
  })

  constructor(private router: Router,private  authService: AuthService,  @Inject(MAT_DIALOG_DATA) public data: DialogData,  public dialogRef: MatDialogRef<TwoFactorAuthLoginComponent>) {
    this.errorMessage = "";
  }

  ngOnInit(): void {
  }

  send(){
    this.errorMessage = "";
    if (this.form.valid) {
      this.authService.loginUser2fa(new LoginUser2faDto(this.data.email, this.data.password, this.code.value));
      this.dialogRef.close();
    } else {
      console.log("Not valid")
    }
    
    
  }
}
