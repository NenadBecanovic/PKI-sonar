import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from "./material/material.module";
import { HeaderComponent } from './utils/header/header.component';
import { WelcomeComponent } from './utils/welcome/welcome.component';
import { LoginComponent } from './utils/login/login.component';
import {ReactiveFormsModule} from "@angular/forms";
import { CertificateOverviewComponent } from './certificate-overview/certificate-overview.component';
import { CertificateComponent } from './certificate-overview/components/certificate/certificate.component';
import { SearchComponent } from './certificate-overview/components/search/search.component';
import { NewCertificateComponent } from './new-certificate/new-certificate.component';

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
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
