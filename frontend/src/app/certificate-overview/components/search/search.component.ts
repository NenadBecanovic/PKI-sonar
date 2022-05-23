import { Component, OnInit } from '@angular/core';
import {CertificateService} from "../../../shared/services/certificate.service";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  public SEARCH_PATTERN: string = "^[a-zA-Z0-9' @.]*$";
  public selectedFilter: string = '';
  public searchText: string = '';
  constructor(private _certificateService: CertificateService) { }

  ngOnInit(): void {
  }

  onChipClick(event: string){
    if(event == this.selectedFilter){
      this.selectedFilter = ''
    }else{
      this.selectedFilter = event;
      this._certificateService.filterByType(this.selectedFilter);
    }
  }

  searchCertificate() {
    this._certificateService.search(this.searchText);
  }
}
