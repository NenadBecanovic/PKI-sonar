import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-passwordless-login-validation',
  templateUrl: './passwordless-login-validation.component.html',
  styleUrls: ['./passwordless-login-validation.component.css']
})
export class PasswordlessLoginValidationComponent implements OnInit {

 
  public token: string | null = "";
  public isSuccess: boolean = true;
  public loginUserChanged: Subscription;

  constructor(private _route: ActivatedRoute, private authService: AuthService, private _router: Router) { 
    this.token = this._route.snapshot.paramMap.get('token');
    if (this.token === null) {
      this.token = ""
    }
    console.log(this.token)
    this.authService.loginWithEmail(this.token);
    
    this.loginUserChanged = this.authService.logInUserChanged.subscribe({
      next: (res) => {
      },
      error: (error: HttpErrorResponse) => {
        this.isSuccess = false;
      }
    })
  }

  ngOnInit(): void {
  }

}
