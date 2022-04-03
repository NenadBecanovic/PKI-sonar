import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {map, Observable, startWith} from "rxjs";
import {BreakpointObserver} from "@angular/cdk/layout";
import {StepperOrientation} from "@angular/cdk/stepper";
import {CertificateDataService} from "./certificate-data.service";
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

    this.certKeyUsageFormGroup = new FormGroup({});
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
    }else{
      this.issuer = new FormControl('',Validators.required);
      this.certDataFormGroup.setControl('issuer', this.issuer);
    }
  }

  onCreate() {
    this.router.navigate(['overview']).then();
  }
}
