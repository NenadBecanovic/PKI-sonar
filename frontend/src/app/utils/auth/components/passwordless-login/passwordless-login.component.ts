import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-passwordless-login',
  templateUrl: './passwordless-login.component.html',
  styleUrls: ['./passwordless-login.component.css']
})
export class PasswordlessLoginComponent implements OnInit {
  public errorMessage: string = "";
  public hide = true;
  public email: FormControl = new FormControl('', [Validators.required, Validators.email]);
  public form: FormGroup = new FormGroup({
    'email': this.email
  })

  constructor(private authService: AuthService, private router: Router) { }


  ngOnInit(): void {
  }

  send(){
    this.authService.requestLoginWithEmail(this.email.value).subscribe((response) => {
      console.log(response);
      //snackbar u odnosu na odgovor: zao nam je ne postoji nalog sa tim emailom ili check your email for further steps
      this.router.navigate([''])
    })
  }

}
