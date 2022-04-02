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
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {MatTableModule} from "@angular/material/table";
import { CertificateOverviewComponent } from './certificate-overview/certificate-overview.component';
import { CertificateComponent } from './certificate-overview/certificate/certificate.component';
import {MatCardModule} from "@angular/material/card";
import { SearchComponent } from './certificate-overview/search/search.component';
import {MatChipsModule} from "@angular/material/chips";
import { NewCertificateComponent } from './new-certificate/new-certificate.component';
import {MatStepperModule} from "@angular/material/stepper";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from '@angular/material/core';

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
    MatNativeDateModule,
    MatOptionModule,
    MatSelectModule,
    MatInputModule,
    MatTableModule,
    MatCardModule,
    MatChipsModule,
    MatStepperModule,
    MatCheckboxModule,
    MatAutocompleteModule,
    MatDatepickerModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
