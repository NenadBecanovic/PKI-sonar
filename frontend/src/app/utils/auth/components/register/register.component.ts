import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {HttpErrorResponse} from "@angular/common/http";
import {LoginUserDto} from "../../dtos/LoginUser.dto";
import {RegisterUsetDto} from "../../dtos/RegisterUset.dto";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  public errorMessage: string;
  public hide = true;
  public typeForm: FormGroup;
  public userDataForm: FormGroup;
  public authDataForm: FormGroup;
  public emailCtrl: FormControl;
  public passwordCtrl: FormControl;
  public loginUserChanged: Subscription;
  public entityTypeCtrl: FormControl;
  public nameCtrl: FormControl;
  public surnameCtrl: FormControl;
  public organizationUnitCtrl: FormControl;

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

    this.entityTypeCtrl = new FormControl("", Validators.required);
    this.nameCtrl = new FormControl("", Validators.required);
    this.surnameCtrl = new FormControl("", Validators.required);
    this.organizationUnitCtrl = new FormControl("", Validators.required);
    this.emailCtrl = new FormControl('', [Validators.required, Validators.email]);
    this.passwordCtrl = new FormControl();

    this.typeForm = new FormGroup({
      'entityTypeCtrl': this.entityTypeCtrl,
    })

    this.userDataForm = new FormGroup({
      'nameCtrl': this.nameCtrl,
      'surnameCtrl': this.surnameCtrl,
      'organizationUnitCtrl': this.organizationUnitCtrl,
    })

    this.authDataForm = new FormGroup({
      'emailCtrl': this.emailCtrl,
      'passwordCtrl': this.passwordCtrl
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

  onTypeChange() {
    if(this.entityTypeCtrl.value == "USER"){
      this.nameCtrl = new FormControl("", Validators.required);
      this.surnameCtrl = new FormControl("", Validators.required);
      this.organizationUnitCtrl = new FormControl("");
    }else{

      this.nameCtrl = new FormControl("");
      this.surnameCtrl = new FormControl("");
      this.organizationUnitCtrl = new FormControl("", Validators.required);
    }
    this.userDataForm = new FormGroup({
      'nameCtrl': this.nameCtrl,
      'surnameCtrl': this.surnameCtrl,
      'organizationUnitCtrl': this.organizationUnitCtrl,
    })
  }

  onRegister() {
    if(this.authDataForm.valid){
      let user = new RegisterUsetDto(this.entityTypeCtrl.value, this.nameCtrl.value, this.surnameCtrl.value,
        this.organizationUnitCtrl.value, this.emailCtrl.value, this.passwordCtrl.value);

      console.log(user)
      this.authService.register(user).subscribe({
        next: (res) =>{
          this.router.navigate(['login']).then();
        },
      error:(error: HttpErrorResponse)=>{
          this.errorHandler(error);
      }});
    }
  }
}
