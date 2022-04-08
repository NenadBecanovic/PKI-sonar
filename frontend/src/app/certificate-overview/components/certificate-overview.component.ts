import {Component, OnDestroy, OnInit} from '@angular/core';
import {CertificateService} from "../../shared/services/certificate.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-certificate-overview',
  templateUrl: './certificate-overview.component.html',
  styleUrls: ['./certificate-overview.component.css']
})
export class CertificateOverviewComponent implements OnInit, OnDestroy {
  public certificates: Array<any> = Array<any>();
  public certificatesChanged: Subscription;

  constructor(private _certificateService: CertificateService) {
    this.certificatesChanged = this._certificateService.certificatesChange.subscribe((response) =>{
      this.certificates = response;
    })
  }

  ngOnInit(): void {
    this._certificateService.getAll().subscribe((response) =>{
      this.certificates = response;
    })
  }

  ngOnDestroy(): void {
    this.certificatesChanged.unsubscribe();
  }

}
