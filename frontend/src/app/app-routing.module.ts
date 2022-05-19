import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {WelcomeComponent} from "./utils/welcome/welcome.component";
import {LoginComponent} from "./utils/auth/components/login/login.component";
import {CertificateOverviewComponent} from "./certificate-overview/components/certificate-overview.component";
import {NewCertificateComponent} from "./new-certificate/components/new-certificate.component";
import {RegisterComponent} from "./utils/auth/components/register/register.component";
import { AccountValidationComponent } from './account-validation/components/account-validation.component';
import { PageNotFoundComponent } from './utils/page-not-found/page-not-found.component';
import { PasswordResetComponent } from './password-reset/components/password-reset.component';

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'overview', component: CertificateOverviewComponent},
  {path: 'new', component: NewCertificateComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'account-validation/:token', component: AccountValidationComponent},
  {path: 'password-reset/:token', component: PasswordResetComponent},
  {path: '**', component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
