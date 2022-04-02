import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public loggedInUser: string | null = null;
  public isButtonVisible: boolean = true;
  constructor() {
  }

  ngOnInit(): void {
    this.loggedInUser = localStorage.getItem('role');
    this.hideLogIn();
  }

  hideLogIn(){
    this.loggedInUser = localStorage.getItem('role');
    console.log(this.loggedInUser)
    if(!this.loggedInUser){
      this.isButtonVisible = true;
    }else{
      this.isButtonVisible = false;
    }
  }


  onLogOut() {
    this.isButtonVisible = true
    this.loggedInUser = null;
    localStorage.clear();
  }

  onLogIn() {
    this.isButtonVisible = false
  }
}
