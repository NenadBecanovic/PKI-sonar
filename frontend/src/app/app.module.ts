import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from "./material/material.module";
import { HeaderComponent } from './utils/header/header.component';
import { WelcomeComponent } from './utils/welcome/welcome.component';
import { LoginComponent } from './utils/auth/components/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { CertificateOverviewComponent } from './certificate-overview/components/certificate-overview.component';
import { CertificateComponent } from './certificate-overview/components/certificate/certificate.component';
import { SearchComponent } from './certificate-overview/components/search/search.component';
import { NewCertificateComponent } from './new-certificate/components/new-certificate.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {MatDividerModule} from "@angular/material/divider";
import {RegisterComponent} from "./utils/auth/components/register/register.component";
import {AuthInterceptor} from "./utils/auth/auth.interceptor";
import { AccountValidationComponent } from './account-validation/components/account-validation.component';
import { PageNotFoundComponent } from './utils/page-not-found/page-not-found.component';
import { PasswordResetComponent } from './password-reset/components/password-reset.component';
import { AccountRecoveryComponent } from './account-recovery/components/account-recovery.component';
import { PasswordlessLoginComponent } from './utils/auth/components/passwordless-login/passwordless-login.component';
import { PasswordlessLoginValidationComponent } from './utils/auth/components/passwordless-login-validation/passwordless-login-validation.component';
import { ChangePasswordComponent } from './utils/auth/components/change-password/change-password.component';
import { TwoFactorAuthLoginComponent } from './utils/auth/components/two-factor-auth-login/two-factor-auth-login.component';
import { DisplayTwoFactorAuthSecretComponent } from './utils/auth/components/display-two-factor-auth-secret/display-two-factor-auth-secret.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    WelcomeComponent,
    LoginComponent,
    CertificateOverviewComponent,
    CertificateComponent,
    SearchComponent,
    NewCertificateComponent,
    RegisterComponent,
    AccountValidationComponent,
    PageNotFoundComponent,
    PasswordResetComponent,
    AccountRecoveryComponent,
    PasswordlessLoginComponent,
    PasswordlessLoginValidationComponent,
    ChangePasswordComponent,
    TwoFactorAuthLoginComponent,
    DisplayTwoFactorAuthSecretComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatDividerModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  },],
  bootstrap: [AppComponent]
})
export class AppModule { }
