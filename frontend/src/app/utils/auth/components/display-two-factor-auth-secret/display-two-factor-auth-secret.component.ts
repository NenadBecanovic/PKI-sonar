import {Inject, ViewEncapsulation} from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

export interface DialogData {
  secret: string;
}

@Component({
  selector: 'app-display-two-factor-auth-secret',
  templateUrl: './display-two-factor-auth-secret.component.html',
  styleUrls: ['./display-two-factor-auth-secret.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DisplayTwoFactorAuthSecretComponent implements OnInit {
  public secret = ""
  public secretqr = ""
  constructor(private _authService: AuthService, private _snackBar: MatSnackBar, @Inject(MAT_DIALOG_DATA) public data: DialogData,  public dialogRef: MatDialogRef<DisplayTwoFactorAuthSecretComponent>) { }

  ngOnInit(): void {
    this._authService.enable2fa().subscribe((response) => {
      this._snackBar.open("Two-Factor Authentication is enabled.", "OK", {
        panelClass: ["black-snackbar"]
      });
      this.secretqr = response;
      var parts = this.secretqr.split("=", 2);
      this.secret = parts[1].split("&", 2)[0];
    });
  }

  close(){
    this.dialogRef.close();
  }

}
