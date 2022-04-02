import { Component, OnInit } from '@angular/core';
import {Form, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";
import {CertificateDateService} from "./certificate-date.service";
import {Router} from "@angular/router";

export interface User{
  name: string
}
@Component({
  selector: 'app-new-certificate',
  templateUrl: './new-certificate.component.html',
  styleUrls: ['./new-certificate.component.css']
})
export class NewCertificateComponent implements OnInit {
  public keyUsages: string[] = [];
  public extensions: string[] = [];
  public issuerOptions: User[] = [{name:'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];
  public subjectOptions: User[] = [{name: 'Neko 1'}, {name: 'Neko 2'}, {name: 'Neko 3'}];
  public firstCtrl: FormControl;
  public issuer: FormControl;
  public subject: FormControl;
  public validity: FormControl;
  public firstFormGroup: FormGroup;
  public secondFormGroup: FormGroup;
  public thirdFormGroup: FormGroup;
  public fourthFormGroup: FormGroup;
  public filteredOptionsIssuer: Observable<User[]>;
  public filteredOptionsSubject: Observable<User[]>;

  stepperOrientation: Observable<StepperOrientation>;

  constructor(private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver, private certificateDataService: CertificateDateService, private router: Router) {

    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));

    this.firstCtrl = new FormControl('', Validators.required);
    this.firstFormGroup = new FormGroup({
      'firstCtrl': this.firstCtrl,
    });

    this.issuer = new FormControl('', Validators.required);
    this.subject = new FormControl('', Validators.required);
    this.validity = new FormControl('', Validators.required);
    this.secondFormGroup = new FormGroup({
      'issuer': this.issuer,
      'subject': this.subject,
      'endValidityDate': this.validity,
    });
    this.thirdFormGroup = new FormGroup({});
    this.fourthFormGroup = new FormGroup({});


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

    this.extensions = this.certificateDataService.getCertExtenstions();
    this.keyUsages = this.certificateDataService.getCertKeyUsages();
  }

  ngOnInit(): void {

  }

  displayFn(user: User): string {
    return user && user.name ? user.name : '';
  }

  onNext() {
    console.log(this.firstFormGroup.value)
  }

  onSubmitFirst() {
    console.log('First' + this.firstFormGroup.valid.toString())
    console.log('First' + this.firstFormGroup.value.toString())
    console.log('Second' + this.secondFormGroup.valid.toString())
    console.log('Second' + this.secondFormGroup.value.toString())
  }

  private _filterIssuer(name: string): User[] {
    const filterValue = name.toLowerCase();

    return this.issuerOptions.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  private _filterSubject(name: string): User[] {
    const filterValue = name.toLowerCase();

    return this.subjectOptions.filter(option => option.name.toLowerCase().includes(filterValue));
  }

  onSecondSubmit() {
    console.log(this.secondFormGroup.value)
    console.log(this.secondFormGroup.valid)
    console.log(this.secondFormGroup)
  }

  onTypeChange() {
    console.log("ON")
    if(this.firstCtrl.value == 'ROOT'){
      console.log("CHANGE")
      this.issuer.clearValidators();
      this.secondFormGroup.setControl('issuer', this.issuer);
    }else{
      this.issuer = new FormControl('',Validators.required);
      this.secondFormGroup.setControl('issuer', this.issuer);
    }
  }

  onCreate() {
    this.router.navigate(['overview']).then();
  }
}
