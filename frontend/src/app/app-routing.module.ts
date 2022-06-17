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
import { AccountRecoveryComponent } from './account-recovery/components/account-recovery.component';
import { PasswordlessLoginComponent } from './utils/auth/components/passwordless-login/passwordless-login.component';
import { PasswordlessLoginValidationComponent } from './utils/auth/components/passwordless-login-validation/passwordless-login-validation.component';
import { NewCertGuard } from './shared/auth-guards/new-cert.guard';
import { ViewCertsGuard } from './shared/auth-guards/view-certs.guard';
import { NoAuthGuard } from './shared/auth-guards/no-auth.guard';
import { ChangePasswordComponent } from './utils/auth/components/change-password/change-password.component';

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'login', component: LoginComponent, canActivate: [NoAuthGuard]},
  {path: 'overview', component: CertificateOverviewComponent, canActivate: [ViewCertsGuard]},
  {path: 'new', component: NewCertificateComponent, canActivate: [NewCertGuard]},
  {path: 'register', component: RegisterComponent, canActivate: [NoAuthGuard]},
  {path: 'account-validation/:token', component: AccountValidationComponent, canActivate: [NoAuthGuard]},
  {path: 'account-recovery', component: AccountRecoveryComponent, canActivate: [NoAuthGuard]},
  {path: 'password-reset/:token', component: PasswordResetComponent, canActivate: [NoAuthGuard]},
  {path: 'change-password', component: ChangePasswordComponent, canActivate: [ViewCertsGuard]},
  {path: 'passwordless-login', component: PasswordlessLoginComponent, canActivate: [NoAuthGuard]},
  {path: 'passwordless-login-validation/:token', component:PasswordlessLoginValidationComponent, canActivate: [NoAuthGuard]},
  {path: '**', component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
