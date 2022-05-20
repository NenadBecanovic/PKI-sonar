import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountValidationService } from '../services/account-validation.service';

@Component({
  selector: 'app-account-validation',
  templateUrl: './account-validation.component.html',
  styleUrls: ['./account-validation.component.css']
})
export class AccountValidationComponent implements OnInit {

  public token: string | null = "";
  public isTokenValid: boolean = false;
  public isSuccess: boolean = false;

  constructor(private _route: ActivatedRoute, private validationService: AccountValidationService, private _router: Router) { }

  ngOnInit(): void {
    this.token = this._route.snapshot.paramMap.get('token');
    if (this.token === null) {
      this.token = ""
    }
    console.log(this.token)
    this.validationService.checkToken(this.token).subscribe((response) => {
      this.isTokenValid = response;
      console.log(this.isTokenValid)
      if (this.isTokenValid) {
        this.validationService.validateAccount(this.token).subscribe((response2) => {
          console.log(response2);
          this.isSuccess = response2;
        })
      }

    })
  }

}
