import {Component, OnInit, ViewChild} from '@angular/core';
import {ContentService} from "../../service/content.service";
import {UserSessionService} from "../../service/user.session.service";
import {Content} from "../../model/content";
import { StateService, Transition } from "@uirouter/angular";
import * as moment from 'moment';
import {DialogService} from "ng2-bootstrap-modal";
import {SaveSuccessDialogComponent} from "../dialog/save-success/save-success.dialog.component";
import {UploadService} from "app/service/upload.service";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-company-media-edit',
  templateUrl: './company-media.edit.component.html',
  styleUrls: ['./company-media.edit.component.css']
})
export class CompanyMediaEditComponent implements OnInit {
  public static DATE_FORMAT:string='DD-MM-YYYY';
  companyId:string;
  contentId:string;
  errorMap:any={};
  disableSave:boolean=false;
  content:Content=<Content>{status:'SAVED',type:'LINK'};
  loaded:boolean=false;
  startDateConfig={disableKeypress:true,drops:'up',format:CompanyMediaEditComponent.DATE_FORMAT};
  endDate:string;
  startDate:string;
  @ViewChild('tblFileInput') tblFileInput;
  thumbnail:string;
  constructor(public contentService:ContentService,
              public userSessionService:UserSessionService,
              public state:StateService,
              public uploadService:UploadService,
              public dialogService:DialogService,
              public transition:Transition) {
    this.companyId=this.userSessionService.getCompany().id;
    this.contentId=this.transition.params().contentId;
  }

  ngOnInit() {
    if(this.contentId)
      this.contentService.getCompanyContentByIdForAdmin(this.contentId).subscribe(
        (content:Content)=>{
          this.startDate=content.startDate?moment(content.startDate).format(CompanyMediaEditComponent.DATE_FORMAT):null;
          this.endDate=content.endDate?moment(content.endDate).format(CompanyMediaEditComponent.DATE_FORMAT):null;
          this.content= content;
          this.loaded=true;
          this.thumbnail=content.thumbnail;
        }
      );
  }

  submit(){
    if(!this.validate() || this.disableSave){
      return;
    }

    this.disableSave=true;
    if(this.content.type == 'LINK' && this.thumbnail && this.content.thumbnail != this.thumbnail)
      this.upload(this.tblFileInput.nativeElement).subscribe(res=>{
        this.thumbnail=res;
        this.content.thumbnail=res;
        this.save();
      });
    else
      this.save();
  }

  private save() {
    console.log(this.content);
    this.content.companyId=this.companyId;
    if(this.content.type == 'YOUTUBE_VIDEO'){
      let url = new URL(this.content.url);
      let videoId = url.searchParams.get("v");
      this.content.thumbnail="http://img.youtube.com/vi/"+videoId+"/maxresdefault.jpg";
    }else if(this.content.type == 'LINK' && !this.content.thumbnail){
      this.content.thumbnail="assets/images/article_default.png";
    }
    this.content.startDate=this.convertToDateObject(this.startDate);//moment(this.startDate,CompanyMediaEditComponent.DATE_FORMAT).toDate();
    this.content.endDate=this.convertToDateObject(this.endDate);//moment(this.endDate,CompanyMediaEditComponent.DATE_FORMAT).toDate();
    this.contentService.createOrUpdateContent(this.content).subscribe(res=>{
      this.dialogService.addDialog(SaveSuccessDialogComponent).subscribe(a=>this.state.go("app.company-admin.multimedia"));
    },error2 => this.disableSave=false);
  }

  private validate(){
    let isValid=true;
    this.errorMap={};
    if(!this.content.type){
      this.errorMap.type=true;
      isValid=false;
    }

    if(!this.content.title){
      this.errorMap.title=true;
      isValid=false;
    }
    if(!this.content.status){
      this.errorMap.status=true;
      isValid=false;
    }

    if(!this.content.url){
      this.errorMap.url=true;
      isValid=false;
    }else{
      try{
        new URL(this.content.url)
      }catch(e){
        this.errorMap.url=true;
        isValid=false;
      }

      if(this.content.type == 'YOUTUBE_VIDEO' && !new URL(this.content.url).searchParams.get("v")){
        this.errorMap.url=true;
        isValid=false;
      }
    }
    if(!this.startDate){
      this.errorMap.startDate=true;
      isValid=false;
    }
    if(!this.endDate){
      this.errorMap.endDate=true;
      isValid=false;
    }

    /*if(this.content.type == 'LINK' && !this.thumbnail){
      this.errorMap.tblErrorMessage="Article thumbnail cannot be empty";
      isValid=false;
    }*/

    if(this.endDate && this.startDate && this.convertToDateObject(this.startDate) >= this.convertToDateObject(this.endDate)){
      this.errorMap.endDate=true;
      this.errorMap.startDate=true;
      isValid=false;
    }
    return isValid;
  }

  fileChangeEvent(fileInput: any){
    if (fileInput.target.files && fileInput.target.files[0]) {
      this.thumbnail=fileInput.target.files[0].name;
      this.errorMap.tblErrorMessage=null;
    }
  }

  disableInput(event:any){
    event.preventDefault();
  }

  private convertToDateObject(date:string):Date{
    return date?moment(date,CompanyMediaEditComponent.DATE_FORMAT).toDate():null;
  }

  private upload(fileBrowser:any) :Observable<any>{
    const formData = new FormData();
    formData.append("file", fileBrowser.files[0]);
    formData.append("validate_dim", '360*240');
    this.disableSave=true;
    return this.uploadService.upload(formData)
      .catch((error: Object) => this.handleUploadError(error));
  }

  private handleUploadError(error:any):Observable<any>{
    this.errorMap.tblErrorMessage=error.status==400?"Image width should be 360px and height should be 240px":"Error while uploading the file.";
    this.disableSave=false;
    return Observable.throw(error);
  }
}
