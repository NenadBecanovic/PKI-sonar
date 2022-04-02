import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomeComponent} from "./utils/welcome/welcome.component";
import {LoginComponent} from "./utils/login/login.component";

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
