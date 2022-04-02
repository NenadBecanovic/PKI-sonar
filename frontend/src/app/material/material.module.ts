import { NgModule } from '@angular/core';
import {MatRadioModule} from "@angular/material/radio";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";

const MaterialComponents = [
  MatRadioModule,
  MatToolbarModule,
  MatIconModule,
  MatButtonModule,
  MatGridListModule,
  MatFormFieldModule
]

@NgModule({
  imports: [
    MaterialComponents
  ],
  exports:[MaterialComponents],
  providers:[
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'}}
  ]
})
export class MaterialModule { }
