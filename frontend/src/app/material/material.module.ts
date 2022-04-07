import { NgModule } from '@angular/core';
import {MatRadioModule} from "@angular/material/radio";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatStepperModule} from "@angular/material/stepper";
import {MAT_CHECKBOX_DEFAULT_OPTIONS, MatCheckboxDefaultOptions, MatCheckboxModule} from "@angular/material/checkbox";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from '@angular/material/core';
import {MatChipsModule} from "@angular/material/chips";
import {MatCardModule} from "@angular/material/card";
import {MatOptionModule} from "@angular/material/core";
import {MatSelectModule} from "@angular/material/select";
import {MatInputModule} from "@angular/material/input";
import {MatTableModule} from "@angular/material/table";
import {MAT_SNACK_BAR_DEFAULT_OPTIONS, MatSnackBarModule} from "@angular/material/snack-bar";

const MaterialComponents = [
  MatRadioModule,
  MatToolbarModule,
  MatIconModule,
  MatButtonModule,
  MatGridListModule,
  MatFormFieldModule,
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
  MatDatepickerModule,
]

@NgModule({
  imports: [
    MaterialComponents,
    MatSnackBarModule
  ],
  exports:[MaterialComponents],
  providers:[
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'}},
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 3000, appearance: 'fill'}},
    {provide: MAT_CHECKBOX_DEFAULT_OPTIONS, useValue: {clickAction: 'check-indeterminate'} as MatCheckboxDefaultOptions}
  ]
})
export class MaterialModule { }
