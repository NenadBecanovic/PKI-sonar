import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {AuthService} from "../auth/services/auth.service";
import {UserTokenStateDto} from "../auth/dtos/UserTokenState.dto";
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DisplayTwoFactorAuthSecretComponent } from '../auth/components/display-two-factor-auth-secret/display-two-factor-auth-secret.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public loggedInUser: string | undefined = undefined;
  public isButtonVisible: boolean = true;
  public loggedInUserChanged: Subscription;

  constructor(public matDialog: MatDialog, private router: Router, private _authService: AuthService) {
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

  openTfaDialog(){
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = false;
    dialogConfig.id = "2fa-code-modal";
    dialogConfig.height = "200px";
    dialogConfig.width = "28%";
    //dialogConfig.data = { email: emailp, password: passwordp}
    const tfaModal = this.matDialog.open(DisplayTwoFactorAuthSecretComponent, dialogConfig);
  }
}
