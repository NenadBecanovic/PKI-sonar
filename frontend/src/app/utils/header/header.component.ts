import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {AuthService} from "../auth/services/auth.service";
import {UserTokenStateDto} from "../auth/dtos/UserTokenState.dto";
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DisplayTwoFactorAuthSecretComponent } from '../auth/components/display-two-factor-auth-secret/display-two-factor-auth-secret.component';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public loggedInUser: string | undefined = undefined;
  public isButtonVisible: boolean = true;
  public loggedInUserChanged: Subscription;
  public is2faEnabled : boolean = false;

  constructor(public matDialog: MatDialog, private _snackBar: MatSnackBar, private router: Router, private _authService: AuthService) {
    this.loggedInUserChanged = this._authService.logInUserChanged.subscribe((response: UserTokenStateDto)=>{
      this.loggedInUser = response.roles.pop();
    })

    this.router.events.subscribe((val) => {
      if (this.router.url.includes('login') && !this.loggedInUser) {
        this.isButtonVisible = false;
      } else if (this.router.url.includes('register') && !this.loggedInUser) {
        this.isButtonVisible = true;
      }else{
        this.isButtonVisible = true;
      }
    })
  }

  ngOnInit(): void {
    this._authService.getRole().subscribe((response: string) =>{
      if(response){
        this.isButtonVisible = false;
        this.loggedInUser = response;
        this._authService.getUsing2fa().subscribe((response: boolean) => {
          this.is2faEnabled = response;
        });
      }
    });
  }


  onLogOut() {
    this.isButtonVisible = true
    this.loggedInUser = undefined;
    localStorage.clear();
    this.router.navigate(['/']).then();
  }

  changeButtonVisibility() {
    this.isButtonVisible = true;
  }

  enable2fa(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.id = "2fa-code-modal";
    dialogConfig.height = "500px";
    dialogConfig.width = "28%";
    
    this.is2faEnabled=true;
    const tfaModal = this.matDialog.open(DisplayTwoFactorAuthSecretComponent, dialogConfig);
  }

  disable2fa(){
    this._authService.disable2fa().subscribe((response: string) => {
      let a = response;
      this._snackBar.open("Two-Factor Authentication is disabled.", "OK", {
        panelClass: ["black-snackbar"]
      });
      this.is2faEnabled=false;
    });
  }
}
