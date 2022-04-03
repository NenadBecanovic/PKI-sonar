import {Component, Input, OnInit} from '@angular/core';
import {Certificate} from "../../model/certificate.model";
import {DateRange} from "../../model/dateRange.model";


@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.css']
})
export class CertificateComponent implements OnInit {
  @Input() certificate: Certificate = new Certificate('New', 'New', new DateRange(new Date(), new Date()), '');

  constructor() {
  }

  ngOnInit(): void {
  }

}
