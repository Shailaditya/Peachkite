
import {Component, OnInit} from "@angular/core";
import {FaqService} from "../../service/faq.service";
import { StateService, Transition } from "@uirouter/angular";
import {Faq} from "../../model/faq";
import {UserSessionService} from "../../service/user.session.service";
import {HttpResponse} from "@angular/common/http";
import {DialogService} from "ng2-bootstrap-modal";
import {FaqLimitErrorDialogComponent} from "../dialog/faq-limit-error/faq-limit-error.dialog.component";
import {SaveSuccessDialogComponent} from "../dialog/save-success/save-success.dialog.component";
@Component({
  selector: 'app-company-faq-edit',
  templateUrl: './company-faq.edit.component.html',
  styleUrls: ['./company-faq.edit.component.css']
})
export class CompanyFaqEditComponent implements OnInit {

  faqId:string;
  faq:Faq;
  errorMap:any={};
  disableSave=false;
  constructor(public faqService:FaqService,
              public state:StateService,
              public dialogService:DialogService,
              public userSessionService:UserSessionService,
              public transition:Transition) {
    this.faqId=this.transition.params().faqId;
  }

  ngOnInit() {
    if(this.faqId){
      this.faqService.getFaqById(this.faqId).subscribe((faq:Faq)=>{
        this.faq=faq;
      });
    }else{
      this.faq=<Faq>{companyId:this.userSessionService.getCompany().id,status:'Saved'};
    }
  }

  save(){
    if(this.disableSave || !this.validate())
      return;
    this.disableSave=true;
    this.faqService.saveFaq(this.faq).subscribe((res:HttpResponse<any>)=>{
      this.dialogService.addDialog(SaveSuccessDialogComponent).subscribe(a=>{
        this.state.go("app.company-admin.faq");
      });
    },error2 => {
      this.disableSave=false;
      if(error2.status == 409)
        this.dialogService.addDialog(FaqLimitErrorDialogComponent).subscribe();
    });
  }

  validate():boolean{
    if(!this.faq.response){
      this.errorMap.response=true;
      return false;
    }
    if(!this.faq.question){
      this.errorMap.question=true;
      return false;
    }
    return true;
  }

}
