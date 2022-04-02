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

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    WelcomeComponent,
    LoginComponent,
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MaterialModule,
        ReactiveFormsModule,
        MatOptionModule,
        MatSelectModule,
        MatInputModule,
        MatTableModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
