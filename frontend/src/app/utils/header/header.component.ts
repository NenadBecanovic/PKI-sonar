import { Component, OnInit } from '@angular/core';
import { Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public loggedInUser: string | null = null;
  public isButtonVisible: boolean = true;
  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.loggedInUser = localStorage.getItem('role');
    this.hideLogIn();
  }

  hideLogIn(){
    this.loggedInUser = localStorage.getItem('role');
    this.isButtonVisible = !this.loggedInUser;
  }


  onLogOut() {
    this.isButtonVisible = true
    this.loggedInUser = null;
    localStorage.clear();
    this.router.navigate(['/']).then();
  }

  onLogIn() {
    this.isButtonVisible = false
  }
}
