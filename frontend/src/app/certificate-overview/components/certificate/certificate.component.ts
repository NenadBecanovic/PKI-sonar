import {Component, Input, OnInit} from '@angular/core';
import {Certificate} from "../../model/certificate.model";
import {CertificateService} from "../../../shared/services/certificate.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {saveAs} from "file-saver";


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
          this._snackBar.open("Certificate successfully revoked.", "OK", {
            panelClass: ["black-snackbar"]
          });
        } else {
          this._snackBar.open("Certificate could not be revoked.", "OK", {
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
          this._snackBar.open("Certificate is valid!", "OK",{
            panelClass: ["black-snackbar"]});
        }else{
          this._snackBar.open("Certificate is not valid.", "OK", {
            panelClass: ["black-snackbar"]
          });
        }
      }
    });
  }

  onDownload() {
    this._certificateService.download(this.certificate.serialNumber).subscribe(blob => saveAs(blob, 'certificate.crt'));
  }
}
