import { Component, OnInit } from '@angular/core';
import {Certificate} from "./model/certificate.model";
import {DateRange} from "./model/dateRange.model";

@Component({
  selector: 'app-certificate-overview',
  templateUrl: './certificate-overview.component.html',
  styleUrls: ['./certificate-overview.component.css']
})
export class CertificateOverviewComponent implements OnInit {
  public certificates: Certificate[] = [
    new Certificate('Subject 1', 'Issuer 1', new DateRange(new Date, new Date), 'INTERMEDIATE'),
    new Certificate('Subject 2', 'Issuer 2', new DateRange(new Date, new Date), 'ROOT'),
    new Certificate('Subject 3', 'Issuer 3', new DateRange(new Date, new Date), 'END'),
    new Certificate('Subject 1', 'Issuer 1', new DateRange(new Date, new Date), 'INTERMEDIATE'),
    new Certificate('Subject 2', 'Issuer 2', new DateRange(new Date, new Date), 'ROOT'),
    new Certificate('Subject 3', 'Issuer 3', new DateRange(new Date, new Date), 'END'),
  ];
  constructor() { }

  ngOnInit(): void {
  }

}
