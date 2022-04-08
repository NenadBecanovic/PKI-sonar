import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  public loggedInUser: string | null = "";

  constructor() {
    this.loggedInUser = localStorage.getItem('token');
  }

  ngOnInit(): void {
  }

}
