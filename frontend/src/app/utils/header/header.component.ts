import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  public loggedInUser: string | null = null;
  public isButtonVisible: boolean = true;
  constructor(private router: Router, private route: ActivatedRoute) {
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
      // this.router.navigate(['/']).then();
    }else{
      console.log(this.route)
      this.isButtonVisible = false;
      // this.router.navigate(['/overview']).then();
    }
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
