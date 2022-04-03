import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  public selectedFilter: string = '';
  constructor() { }

  ngOnInit(): void {
  }

  onChipClick(event: string){
    if(event == this.selectedFilter){
      this.selectedFilter = ''
    }else{
      this.selectedFilter = event;
    }
  }

}
