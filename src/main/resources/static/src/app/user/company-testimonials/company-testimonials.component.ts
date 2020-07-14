import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CompanyService} from "../../service/company.service";
import {Testimonial} from "../../model/testimonial";
import * as moment from 'moment';
import {UserSessionService} from "../../service/user.session.service";

@Component({
  selector: 'app-company-testimonials',
  templateUrl: './company-testimonials.component.html',
  styleUrls: ['./company-testimonials.component.css']
})
export class CompanyTestimonialsComponent implements OnInit {

  @Input() companyId: string;
  @Input() isRated: boolean;
  @Output() pageEvent = new EventEmitter<void>();
  pageNumber:number=0;
  pageSize:number=4;
  isLast:boolean=false;
  testimonials:Testimonial[][]=[];
  isLoggedIn:boolean=false;
  noData:boolean=true;
  constructor(private companyService :CompanyService,
              public userSessionService :UserSessionService) {
    this.isLoggedIn=this.userSessionService.isLoggedIn();
  }

  ngOnInit() {
    this.getData();
  }

  next(){
    if(this.userSessionService.isLoggedIn()){
      this.pageNumber++;
      this.getData();
    }else{
      this.pageEvent.emit();
    }
  }

  getTime(date:Date){
    return moment(date).fromNow();
  }

  private getData(){
    this.companyService
      .getTestimonials(this.companyId,this.pageNumber,this.pageSize)
      .subscribe((res:any)=>{
        this.isLast=res.last;
        if(res.totalElements>0){
          this.noData=false;
        }
        let testimonials:Testimonial[]=<Testimonial[]>res.content;
        /*if(testimonials.length > 2){
          while(testimonials.length>2)
            this.testimonials.push(testimonials.splice(0,2));
        }else {
          this.testimonials.push(testimonials);
        }*/
        while(testimonials.length>=2){
          this.testimonials.push(testimonials.splice(0,2));
        }
        if(testimonials.length == 1)
          this.testimonials.push([testimonials[0]]);
        this.testimonials=this.testimonials.concat(<Testimonial[]>res.content);
      });
  }

}
