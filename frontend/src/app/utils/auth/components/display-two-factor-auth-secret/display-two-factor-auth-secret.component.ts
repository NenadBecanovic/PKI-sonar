import {ViewEncapsulation} from '@angular/core';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-display-two-factor-auth-secret',
  templateUrl: './display-two-factor-auth-secret.component.html',
  styleUrls: ['./display-two-factor-auth-secret.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DisplayTwoFactorAuthSecretComponent implements OnInit {
  public secret = "YQQIXDH6XVXXPHJXLAEDF3GUWMBDQ3FE"
  constructor() { }

  ngOnInit(): void {
  }

}
