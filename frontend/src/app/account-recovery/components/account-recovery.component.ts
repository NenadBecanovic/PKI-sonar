import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AccountRecoveryService } from '../services/account-recovery.service';

@Component({
  selector: 'app-account-recovery',
  templateUrl: './account-recovery.component.html',
  styleUrls: ['./account-recovery.component.css']
})
export class AccountRecoveryComponent implements OnInit {
  public errorMessage: string = "";
  public hide = true;
  public email: FormControl = new FormControl('', [Validators.required, Validators.email]);
  public form: FormGroup = new FormGroup({
    'email': this.email
  })

  constructor(private validationService: AccountRecoveryService, private router: Router, private _snackBar: MatSnackBar) { }


  ngOnInit(): void {
  }

  send(){
    this.validationService.recoverAccount(this.email.value).subscribe((response) => {
      console.log(response);
      this._snackBar.open("Success. Further steps will be sent to you via email if account exist.", "OK", {
        panelClass: ["black-snackbar"]
      });
      this.router.navigate(['/login'])
    })
  }
}
