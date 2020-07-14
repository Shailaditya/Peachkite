import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {UploadService} from "../../service/upload.service";
import {DialogService} from "ng2-bootstrap-modal";
import { StateService, Transition } from "@uirouter/angular";
import {Company} from "../../model/company";
import {UserSessionService} from "../../service/user.session.service";
import {CompanyService} from "../../service/company.service";
import {Observable} from "rxjs/Observable";
import {SaveSuccessDialogComponent} from "../../company-admin/dialog/save-success/save-success.dialog.component";

@Component({
  selector: 'app-admin-company-edit',
  templateUrl: './company-edit.component.html',
  styleUrls: ['./company-edit.component.css']
})
export class CompanyEditComponent implements OnInit {

  disabled=false;
  industries:string[]=[
    "Banking",
    "Capital Markets",
    "Cement",
    "Chemicals",
    "Construction",
    "Consumer Goods",
    "Electrical/Electronic Manufacturing",
    "Financial Services",
    "Government Administration",
    "Hospital & Health Care",
    "Information Technology and Services",
    "Insurance",
    "Internet",
    "Investment Management",
    "Leisure, Travel & Tourism",
    "Logistics and Supply Chain",
    "Market Research",
    "Mechanical or Industrial Engineering",
    "Outsourcing/Offshoring",
    "pharmaceutical",
    "Pharmaceuticals",
    "Real Estate",
    "Research",
    "Automotive",
    "Telecommunications",
    "Management Consulting",
    "Group",
    "Retail",
    "Cosmetics",
    "Food & Beverages",
    "Computer Software",
    "Medical Devices"
  ];
  logoFileName:string;
  aboutUsFileName:string;
  error:any={};
  company:Company;
  companyId:string;
  @ViewChild('logoFileInput') logoFileInput;
  @ViewChild('aboutUsFileInput') aboutUsFileInput;
  constructor( public uploadService:UploadService,
               public companyService:CompanyService,
               public stateService:StateService,
               public dialogService:DialogService,
               public transition:Transition) {
    this.companyId=this.transition.params().companyId;
  }

  ngOnInit() {

    this.companyId?this.companyService.getCompanyById(this.companyId).subscribe((res:Company)=>{
      this.company=res;
      this.logoFileName=this.company.logo;
      this.aboutUsFileName=this.company.aboutUsImage;
    }):this.company=<Company>{};
  }

  disableInput(event:any){
    event.preventDefault();
  }

  fileChangeEvent(fileInput: any,type:string){
    if (fileInput.target.files && fileInput.target.files[0]) {
      if(type=='logo'){
        this.logoFileName=fileInput.target.files[0].name;
        this.error.logoErrorMessage=null;
      }else{
        this.aboutUsFileName=fileInput.target.files[0].name;
        this.error.aboutUsErrorMessage=null;
      }
    }
  }

  submit(){
    if(!this.isValid()){
      return;
    }

    if(this.disabled)
      return;
    this.disabled=true;

    /*if (this.logoFileInput.nativeElement.files && this.logoFileInput.nativeElement.files[0]) {
      this.upload('100*100',this.logoFileInput.nativeElement).subscribe((res:any)=>{
        this.company.logo=res;
        this.saveCompanyDetails();
      },error=>{
        console.error("error while uploading the file.");
        this.disabled=false;
      });
    }else{
      this.saveCompanyDetails();
    }*/
    let uploads=[];
    if(this.aboutUsFileName != null && this.aboutUsFileName != this.company.aboutUsImage)
      uploads.push(this.upload('350*250',this.aboutUsFileInput.nativeElement,'aboutus'));
    if(this.logoFileName != this.company.logo)
      uploads.push(this.upload('100*100',this.logoFileInput.nativeElement,'logo'));

    if(uploads.length){
      Observable.forkJoin(uploads).subscribe((data:any[])=>{
        if(this.aboutUsFileName != null && data.length && this.aboutUsFileName != this.company.aboutUsImage){
          this.company.aboutUsImage=data.shift();
        }
        if(data.length && this.logoFileName != this.company.logo){
          this.company.logo=data.shift();
        }
        this.saveCompanyDetails();
      });
    }else{
      this.saveCompanyDetails();
    }
    /*this.upload('100*100',this.logoFileInput.nativeElement,'logo').subscribe((res:any)=>{
      this.company.logo=res;
      this.upload('350*250',this.aboutUsFileInput.nativeElement,'aboutus').subscribe((res:any)=>{
        this.company.aboutUsImage=res;
        this.saveCompanyDetails();
      },error=>{
        if(error.statusCode==400)
          this.error.aboutUsErrorMessage="Image width should be 350px and height should be 250px.";
        else
          this.error.aboutUsErrorMessage="Error while uploading the file.";
        console.error("error while uploading the aboutUs File.");
        this.disabled=false;
      });
    },error=>{
      if(error.statusCode==400)
        this.error.logoErrorMessage="Image width should be 100px and height should be 100px.";
      else
        this.error.logoErrorMessage="Error while uploading the file.";
      console.error("error while uploading the logoFile.");
      this.disabled=false;
    });*/
  }

  private upload(dim:string,fileBrowser:any,type:string) :Observable<any>{
    const formData = new FormData();
    formData.append("file", fileBrowser.files[0]);
    formData.append("validate_dim", dim);
    this.disabled=true;
    return this.uploadService.upload(formData)
      .catch((error: Object) => this.handleUploadError(error,type));
  }

  private handleUploadError(error:any,type:string):Observable<any>{
    if(type=='logo'){
      this.error.logoErrorMessage=error.status==400?"Image width should be 100px and height should be 100px.":"Error while uploading the file.";
    }else{
      this.error.aboutUsErrorMessage=error.status==400?"Image width should be 350px and height should be 250px":"Error while uploading the file.";
    }
    this.disabled=false;
    return Observable.throw(error);
  }

  private saveCompanyDetails():void{
    this.companyService.save(this.company).subscribe((company:Company)=>{
      this.dialogService.addDialog(SaveSuccessDialogComponent).subscribe();
      this.company=company;
      this.disabled=false;
      this.logoFileName=this.company.logo;
      this.aboutUsFileName=this.company.aboutUsImage;
      this.stateService.go("app.admin-home");
    },error=>{
      console.error("error");
      this.disabled=false;
    });
  }

  private isValid():boolean{
    this.error={};
    if(!this.company.name)
      this.error.name="Company name cannot be empty.";
    if(!this.logoFileName)
      this.error.logoErrorMessage="Company logo is required.";
    /*if(!this.aboutUsFileName)
      this.error.aboutUsErrorMessage="About image is required is required.";*/
    if(this.aboutUsFileInput && this.aboutUsFileInput.nativeElement.files[0]!=null && this.aboutUsFileInput.nativeElement.files[0].size > 1000000)
      this.error.aboutUsErrorMessage="File size should be less than 1mb.";
    if(this.logoFileInput && this.logoFileInput.nativeElement.files[0]!=null && this.logoFileInput.nativeElement.files[0].size > 1000000)
      this.error.logoErrorMessage="File size should be less than 1mb.";
    return Object.keys(this.error).length?false:true;
  }

}
