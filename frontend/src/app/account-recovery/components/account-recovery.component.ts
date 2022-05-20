import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
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

  constructor(private validationService: AccountRecoveryService, private router: Router) { }


  ngOnInit(): void {
  }

  send(){
    this.validationService.recoverAccount(this.email.value).subscribe((response) => {
      console.log(response);
      this.router.navigate(['/login'])
    })
  }
}
