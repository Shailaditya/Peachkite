import { Component, OnInit } from '@angular/core';
import {ContentService} from "../../service/content.service";
import {UserSessionService} from "../../service/user.session.service";
import {Content} from "../../model/content";
import {StateService} from "@uirouter/angular";
import * as moment from 'moment';
import {CompanyMediaEditComponent} from "./company-media.edit.component";

@Component({
  selector: 'app-company-media',
  templateUrl: './company-media.component.html',
  styleUrls: ['./company-media.component.css']
})
export class CompanyMediaComponent implements OnInit {

  companyId:string;
  contentList:Content[]=[];
  totalPages:number=1;
  pageSize:number=5;
  loaded:boolean=false;
  constructor(public contentService:ContentService,
              public state:StateService,
              public userSessionService:UserSessionService) {
    this.companyId=this.userSessionService.getCompany().id;
  }

  ngOnInit() {
    this.loadData(0);
  }

  loadData(pageNumber:number){
    console.log("CompanyMediaComponent.loadData",pageNumber);
    this.contentService.getCompanyContentForAdmin(this.companyId,pageNumber,this.pageSize).subscribe(
      res=>{
        this.contentList= res.content;
        this.totalPages= res.totalPages;
        this.loaded=true;
      }
    );
  }

  editContent(contentId:string){
    this.state.go("app.company-admin.multimedia-edit",{contentId:contentId});
  }

  addContent(){
    this.state.go("app.company-admin.multimedia-create");
  }

  formatDate(date:Date):string{
    return moment(date).format(CompanyMediaEditComponent.DATE_FORMAT);
  }
}
