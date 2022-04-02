import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomeComponent} from "./utils/welcome/welcome.component";
import {LoginComponent} from "./utils/login/login.component";
import {CertificateOverviewComponent} from "./certificate-overview/certificate-overview.component";
import {NewCertificateComponent} from "./new-certificate/new-certificate.component";

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'overview', component: CertificateOverviewComponent},
  {path: 'new', component: NewCertificateComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
