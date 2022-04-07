import {Component, ElementRef, OnInit} from '@angular/core';
import {Form, FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";
import {CertificateService} from "../services/certificate.service";
import {Router} from "@angular/router";
import {MatCheckbox, MatCheckboxChange} from "@angular/material/checkbox";

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
  public extendedKeyUsages: Array<any> = [];

  public issuerOptions: User[] = [{name:'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];
  public subjectOptions: User[] = [{name: 'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];

  public filteredOptionsIssuer: Observable<User[]>;
  public filteredOptionsSubject: Observable<User[]>;

  public certTypeCtrl: FormControl;
  public certTypeFormGroup: FormGroup;
  public certDataFormGroup: FormGroup;
  public certKeyUsageFormGroup: FormGroup;
  public certExtFormGroup: FormGroup;
  public certExtKeyUsageFormGroup: FormGroup;
  public allChecked: boolean = false;

  public organizationUnitName: FormControl;
  public organizationName: FormControl;
  public issuer: FormControl;
  public subject: FormControl;
  public validity: FormControl;
  public country: FormControl;

  stepperOrientation: Observable<StepperOrientation>;

  constructor(private _elementRef: ElementRef, private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver, private certificateService: CertificateService, private router: Router) {
    this.certificateService.getCertExtensions().subscribe((response: Array<any>) =>{
      this.extensions = response;
      console.log(response)
    });
    this.certificateService.getCertKeyUsages().subscribe((response: Array<any>) =>{
      this.keyUsages = response;
      console.log(response)
    });
    this.certificateService.getCertExtKeyUsages().subscribe((response: Array<any>) => {
      this.extendedKeyUsages = response;
      console.log(response)
    });

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
    this.organizationUnitName = new FormControl('', Validators.required);
    this.organizationName = new FormControl('', Validators.required);
    this.country = new FormControl('', Validators.required);
    this.certDataFormGroup = new FormGroup({
      'issuer': this.issuer,
      'subject': this.subject,
      'endValidityDate': this.validity,
      'organizationUnitName': this.organizationUnitName,
      'organizationName': this.organizationName,
      'country': this.country,
    });

    this.certKeyUsageFormGroup = new FormGroup({
      'keyUsage': new FormArray([])
    });

    this.certExtFormGroup = new FormGroup({
      'extensions': new  FormArray([])
    });

    this.certExtKeyUsageFormGroup = new FormGroup({
      'extKeyUsage': new FormArray([])
    });

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
      this.organizationUnitName.clearValidators();
      this.certDataFormGroup.setControl('issuer', this.issuer);
      this.certDataFormGroup.setControl('organizationUnitName', this.organizationUnitName);
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl(6);
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
    }else if(this.certTypeCtrl.value == 'CA'){
      this.organizationName.clearValidators();
      this.country.clearValidators();
      this.certDataFormGroup.setControl('organizationName', this.organizationName);
      this.certDataFormGroup.setControl('country', this.country);
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl(6);
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
    }else {
      this.issuer = new FormControl('',Validators.required);
      this.certDataFormGroup.setControl('issuer', this.issuer);
    }
  }

  onCreate() {
    let extensions = [];
    let keyUsages = [];
    let extKeyUsages = [];

    for(let ctrl of (this.certExtFormGroup.get('extensions') as FormArray).controls){
      extensions.push(ctrl.value);
    }

    for (let ctrl of (this.certKeyUsageFormGroup.get('keyUsage') as FormArray).controls) {
      keyUsages.push(ctrl.value);
    }

    for (let ctrl of (this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray).controls) {
      extKeyUsages.push(ctrl.value);
    }

    this.certificateService.createdCertificate(
      this.certTypeCtrl.value, this.certDataFormGroup.value, extensions, keyUsages, extKeyUsages).subscribe((response) =>{
      this.router.navigate(['overview']).then();
    })
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


  onExtensionChange(event: MatCheckboxChange) {
    const extensionArray: FormArray = this.certExtFormGroup.get('extensions') as FormArray;
    console.log(event.source.value)
    if (event.checked && event.source.value != "3") {
      extensionArray.push(new FormControl(event.source.value));
    } else if (event.checked && event.source.value == "3") {
      const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
      for(let i = 0; i < this.extendedKeyUsages.length; i++){
        extKeyUsageArray.insert(i, new FormControl(this.extendedKeyUsages[i].id));
      }
      this.allChecked = true;
    } else if (!event.checked && event.source.value == "3") {
      const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
      extKeyUsageArray.clear();
      this.allChecked = false;
    } else {
      let i: number = 0;
      extensionArray.controls.forEach((item: any) => {
        if (item.value == event.source.value) {
          extensionArray.removeAt(i);
          return;
        }
        i++;
      });
    }
  }

  onExtKeyUsageChange(event: MatCheckboxChange) {
    const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
    if (event.checked) {
      extKeyUsageArray.push(new FormControl(event.source.value));
    }else if(event.checked && this.extendedKeyUsages.length == 1){
      const extensionArray: FormArray = this.certExtKeyUsageFormGroup.get('extensions') as FormArray;
      let i: number = 0;
      extensionArray.controls.forEach((item: any) => {
        if (item.value == 3) {
          extensionArray.removeAt(i);
          return;
        }
        i++;
      });
    } else {
      let i: number = 0;
      extKeyUsageArray.controls.forEach((item: any) => {
        if (item.value == event.source.value) {
          extKeyUsageArray.removeAt(i);
          return;
        }
        i++;
      });
    }

    this.allChecked = extKeyUsageArray.length == this.extendedKeyUsages.length;
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

  someChecked() {
    const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
    return !!extKeyUsageArray.length && !this.allChecked;
  }


  checkExtendedKeys(extendedKeyUsageType: any) {
    const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
    let i: number = 0;
    let retVal = false;
    extKeyUsageArray.controls.forEach((item: any) => {
      if (item.value == extendedKeyUsageType) {
        retVal = true;
      }
      i++;
    });
    return retVal;
  }
}
