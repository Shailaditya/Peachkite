import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnInit {

  @Output() pageEvent = new EventEmitter<number>();

  @Input() totalPages:number;

  @Input() currentPage:number;

  pageNumbers=[];

  constructor() { }

  ngOnInit() {
    for(let i=1;i<=(this.totalPages>5?5:this.totalPages);i++)
      this.pageNumbers.push(i);

    console.log("totalPages",this.totalPages);
  }

  pageLoad(pageNumber:number) {
    if(this.currentPage == pageNumber)
      return;
    this.currentPage = pageNumber;
    this.emitPageEvent();
  }

  prev() {
    this.currentPage--;
    this.emitPageEvent();
  }

  next() {
    this.currentPage++;
    this.emitPageEvent();
  }

  private resetPageNumbers(){
    if(this.currentPage > 5){
      this.pageNumbers=[];
      for(let i=(this.currentPage-5);i<=this.currentPage;i++)
        this.pageNumbers.push(i);
    }

  }

  private emitPageEvent(){
    this.pageEvent.emit(this.currentPage-1);
    this.resetPageNumbers();
  }

}
