import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";
import {CertificateDataService} from "./certificate-data.service";
import {Router} from "@angular/router";
import {MatCheckboxChange} from "@angular/material/checkbox";

export interface User{
  name: string
}
@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.css']
})
export class NewCertificateComponent implements OnInit {
  public keyUsages: Array<any> = [];
  public extensions: Array<any> = [];

  public issuerOptions: User[] = [{name:'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];
  public subjectOptions: User[] = [{name: 'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];

  public filteredOptionsIssuer: Observable<User[]>;
  public filteredOptionsSubject: Observable<User[]>;

  public certTypeCtrl: FormControl;
  public certTypeFormGroup: FormGroup;
  public certDataFormGroup: FormGroup;
  public certKeyUsageFormGroup: FormGroup;
  public certExtFormGroup: FormGroup;

  public issuer: FormControl;
  public subject: FormControl;
  public validity: FormControl;

  stepperOrientation: Observable<StepperOrientation>;

  constructor(private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver, private certificateDataService: CertificateDataService, private router: Router) {
    this.extensions = this.certificateDataService.getCertExtensions();
    this.keyUsages = this.certificateDataService.getCertKeyUsages();

    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));

    this.certTypeCtrl = new FormControl('', Validators.required);
    this.certTypeFormGroup = new FormGroup({
      'certTypeCtrl': this.certTypeCtrl,
    });

    this.issuer = new FormControl('', Validators.required);
    this.subject = new FormControl('', Validators.required);
    this.validity = new FormControl('', Validators.required);
    this.certDataFormGroup = new FormGroup({
      'issuer': this.issuer,
      'subject': this.subject,
      'endValidityDate': this.validity,
    });

    this.certKeyUsageFormGroup = new FormGroup({
      'keyUsage': new FormArray([])
    });

    this.certExtFormGroup = new FormGroup({});


    this.filteredOptionsIssuer = this.issuer.valueChanges.pipe(
      startWith(''),
      map(value => (typeof value === 'string' ? value : value.name)),
      map(name => (name ? this._filterIssuer(name) : this.issuerOptions.slice())),
    );

    this.filteredOptionsSubject = this.issuer.valueChanges.pipe(
      startWith(''),
      map(value => (typeof value === 'string' ? value : value.name)),
      map(name => (name ? this._filterSubject(name) : this.issuerOptions.slice())),
    );

  }

  ngOnInit(): void {

  }

  displayFn(user: User): string {
    return user && user.name ? user.name : '';
  }

  private _filterIssuer(name: string): User[] {
    const filterValue = name.toLowerCase();

    return this.issuerOptions.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  private _filterSubject(name: string): User[] {
    const filterValue = name.toLowerCase();

    return this.subjectOptions.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  onTypeChange() {
    if(this.certTypeCtrl.value == 'ROOT'){
      this.issuer.clearValidators();
      this.certDataFormGroup.setControl('issuer', this.issuer);
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl("CERTIFICATE_SIGNING");
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
    }else if(this.certTypeCtrl.value == 'CA'){
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl("CERTIFICATE_SIGNING");
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
    }else {
      this.issuer = new FormControl('',Validators.required);
      this.certDataFormGroup.setControl('issuer', this.issuer);
    }
  }

  onCreate() {
    this.router.navigate(['overview']).then();
  }

  onKeyUsageChange(event: MatCheckboxChange) {
    const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
    if (event.checked) {
      keyUsageArray.push(new FormControl(event.source.value));
    } else {
      let i: number = 0;
      keyUsageArray.controls.forEach((item: any) => {
        if (item.value == event.source.value) {
          keyUsageArray.removeAt(i);
          return;
        }
        i++;
      });
    }
  }

  isChecked(keyUsage: string) {
    let isChecked = false;
    (this.certKeyUsageFormGroup.get('keyUsage') as FormArray).controls.forEach((item: any) => {
      if (item.value == keyUsage) {
        isChecked = true;
      }
    })
    return isChecked;
  }

  isDisabled(keyUsage: string) {
    let isDisabled = false;
    (this.certKeyUsageFormGroup.get('keyUsage') as FormArray).controls.forEach((item: any) => {
      if (item.value == keyUsage && item.disabled) {
        isDisabled = true;
      }
    })
    return isDisabled;
  }
}
