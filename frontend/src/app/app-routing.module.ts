import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomeComponent} from "./utils/welcome/welcome.component";
import {LoginComponent} from "./utils/auth/components/login/login.component";
import {CertificateOverviewComponent} from "./certificate-overview/certificate-overview.component";
import {NewCertificateComponent} from "./new-certificate/new-certificate.component";
import {RegisterComponent} from "./utils/auth/components/register/register.component";

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'overview', component: CertificateOverviewComponent},
  {path: 'new', component: NewCertificateComponent},
  {path: 'register', component: RegisterComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
