import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Content} from "../../model/content";
import {ContentService} from "../../service/content.service";
import {StateService, Transition} from "@uirouter/angular";
import {UserSessionService} from "../../service/user.session.service";

@Component({
  selector: 'app-company-content',
  templateUrl: './company-content.component.html',
  styleUrls: ['./company-content.component.css']
})
export class CompanyContentComponent implements OnInit {

  @Input() companyId: string;
  @Output() showDialog = new EventEmitter<void>();
  private contentSlideCount:number=4
  contentArray:Content[][]=[];
  constructor(public contentService:ContentService ,
              public state:StateService,
              public userSessionService:UserSessionService,
              public transition:Transition,) {
    this.companyId=this.transition.params().companyId;
  }

  ngOnInit() {
    this.contentService.getCompanyContentForUser(this.companyId,0,20).subscribe(
      res=>{
        let contentList:Content[]= res.content;
        let i,j;
        for (i=0,j=contentList.length; i<j; i+=this.contentSlideCount) {
          this.contentArray.push(contentList.slice(i,i+this.contentSlideCount));
        }
      }
    );
  }

  openLink(url:string){
    if(this.userSessionService.isLoggedIn() ){
      window.open(url,'_blank').focus();
    }else {
      this.showDialog.emit();
    }
  }

}
