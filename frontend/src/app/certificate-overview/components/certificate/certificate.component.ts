import {Component, Input, OnInit} from '@angular/core';
import {Certificate} from "../../model/certificate.model";
import {CertificateService} from "../../../shared/services/certificate.service";
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-certificate',
  templateUrl: './certificate.component.html',
  styleUrls: ['./certificate.component.css']
})
export class CertificateComponent implements OnInit {
  @Input() certificate: Certificate = new Certificate();

  constructor(private _certificateService: CertificateService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  onRevoke() {
    this._certificateService.revoke(this.certificate.serialNumber).subscribe({
      next: (response) => {
        if (response) {
          this._snackBar.open("Certificate successfully revoked.", "Ok", {
            panelClass: ["black-snackbar"]
          });
        } else {
          this._snackBar.open("Certificate could not be revoked.", "Ok", {
            panelClass: ["black-snackbar"]
          });
        }
      }
    });
  }

  onValidityCheck() {
    this._certificateService.validityCheck(this.certificate.serialNumber).subscribe({
      next: (response) => {
        if(response){
          this._snackBar.open("Certificate is valid!", "Ok",{
            panelClass: ["black-snackbar"]});
        }else{
          this._snackBar.open("Certificate is not valid.", "Ok", {
            panelClass: ["black-snackbar"]
          });
        }
      }
    });
  }

  onDownload() {
    this._certificateService.download(this.certificate.serialNumber).subscribe();
  }
}
