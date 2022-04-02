import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public hide = true;
  public form: FormGroup;
  public email: FormControl;
  public password: FormControl;

  constructor(private router: Router) {
    this.email = new FormControl('', [Validators.required, Validators.email]);
    this.password = new FormControl();

    this.form = new FormGroup({
      'email': this.email,
      'password': this.password
    })
  }

  ngOnInit(): void {
  }

  getErrorMessage() {
    if (this.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.email.hasError('email') ? 'Not a valid email' : '';
  }

  onLogin() {
    if (this.form.valid) {
      console.log(this.form.value)
      localStorage.setItem('role', 'admin');
    } else {
      console.log("Not valid")
    }
  }
}
