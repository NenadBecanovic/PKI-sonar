import {Component, ElementRef, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {map, Observable, startWith, Subscription} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";
import {CertificateService} from "../../shared/services/certificate.service";
import {Router} from "@angular/router";
import {MatCheckboxChange} from "@angular/material/checkbox";
import {UserService} from "../services/user.service";
import {MatOptionSelectionChange} from "@angular/material/core";
import {UserTokenStateDto} from "../../utils/auth/dtos/UserTokenState.dto";
import {AuthService} from "../../utils/auth/services/auth.service";

export interface User{
  name: string
}
@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.css']
})
export class NewCertificateComponent implements OnInit {
  public loggedInUser: string | undefined = undefined;

  public keyUsages: Array<any> = [];
  public extensions: Array<any> = [];
  public extendedKeyUsages: Array<any> = [];

  public issuerOptions: Array<any> = [];
  public subjectOptions: Array<any> = [];

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
  public now: Date = new Date();

  constructor(private _authService: AuthService, private userService: UserService,private _elementRef: ElementRef, private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver, private certificateService: CertificateService, private router: Router) {
    this._authService.getRole().subscribe((response: string) => {
      this.loggedInUser = response;
    })
    this.certificateService.getCertExtensions().subscribe((response: Array<any>) =>{
      this.extensions = response;
    });
    this.certificateService.getCertKeyUsages().subscribe((response: Array<any>) =>{
      this.keyUsages = response;
    });
    this.certificateService.getCertExtKeyUsages().subscribe((response: Array<any>) => {
      this.extendedKeyUsages = response;
    });

    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));

    this.certTypeCtrl = new FormControl('', Validators.required);
    this.certTypeFormGroup = new FormGroup({
      'certTypeCtrl': this.certTypeCtrl,
    });

    this.issuer = new FormControl("");
    this.subject = new FormControl("");
    this.validity = new FormControl("");
    this.organizationUnitName = new FormControl("");
    this.organizationName = new FormControl("");
    this.country = new FormControl("");
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

  }

  ngOnInit(): void {
  }

  displayFn(user: User): string {
    return user && user.name ? user.name : '';
  }

  setCertDataForm(type: string){
    this.issuer.setValidators(type != "ROOT" ? Validators.required : [])
    this.subject.setValidators(type != "ROOT" ? Validators.required : []);
    this.validity.setValidators(Validators.required);
    this.organizationUnitName.setValidators(type == "INTERMEDIATE" ? Validators.required : []);
    this.organizationName.setValidators(type == "ROOT" ? Validators.required : []);
    this.country.setValidators(type == "ROOT" ? Validators.required : []);
    this.certDataFormGroup.setControl('issuer', this.issuer);
    this.certDataFormGroup.setControl('subject', this.subject);
    this.certDataFormGroup.setControl('validity', this.validity);
    this.certDataFormGroup.setControl('organizationUnitName', this.organizationUnitName);
    this.certDataFormGroup.setControl('organizationName', this.organizationName);
    this.certDataFormGroup.setControl('country', this.country);
  }

  onTypeChange() {
    this.userService.getIssuers(this.certTypeCtrl.value).subscribe((response) =>{
      this.issuerOptions = response;
    })
    this.userService.getSubjects().subscribe((response) => {
      this.subjectOptions = response;
    })
    this.setCertDataForm(this.certTypeCtrl.value);
    if(this.certTypeCtrl.value == 'ROOT'){
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl(6);
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
    }else if(this.certTypeCtrl.value == 'INTERMEDIATE'){
      const keyUsageArray: FormArray = this.certKeyUsageFormGroup.get('keyUsage') as FormArray;
      let newFormControl = new FormControl(6);
      newFormControl.disable();
      keyUsageArray.push(newFormControl);
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
    if (event.checked) {
      extensionArray.push(new FormControl(event.source.value));
      if (event.source.value == "3") {
        const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
        for (let i = 0; i < this.extendedKeyUsages.length; i++) {
          extKeyUsageArray.insert(i, new FormControl(this.extendedKeyUsages[i].id));
        }
        this.allChecked = true;
      }
    } else {
      if(event.source.value == "3"){
        const extKeyUsageArray: FormArray = this.certExtKeyUsageFormGroup.get('extKeyUsage') as FormArray;
        extKeyUsageArray.clear();
        this.allChecked = false;
      }

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
