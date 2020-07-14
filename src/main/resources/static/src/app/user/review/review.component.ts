import { Component, OnInit } from '@angular/core';
import { Response } from '@angular/http';
import { BenefitService,CompanyService,FeedbackService } from '../../service/index';
import { Company,CompanyBenefit } from '../../model/index';
import { StateService, Transition } from "@uirouter/angular";
import {Feedback} from "../../model/feedback";
import {FeedbackThanksDialogComponent} from "../dialog/feeback-thanks/feedback-thanks.dialog.component";
import {DialogService} from "ng2-bootstrap-modal";
import {FeedbackRevisitDialogComponent} from "../dialog/feedback-revisit/feedback-revisit.dialog.component";
import {Util} from "../../util";
import {NoBenefitReviewedDialogComponent} from "../dialog/no-benefit-reviewed/no-benefit-reviewed.dialog.component";
import {UserSessionService} from "../../service/user.session.service";

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {

  company:Company;
  feedback:Feedback=<Feedback>{};
  disableSave:boolean=false;
  errorMap:any={};
  isLoaded:boolean=false;
  isThanksDiaologOpen:boolean=false;
  isRevisitDiaologOpen:boolean=false;
  isNoBenefitReviewedDiaologOpen:boolean=false;
  years:number[]=[];
  constructor(public benefitService:BenefitService,
          public companyService:CompanyService,
          public feedbackService:FeedbackService,
          public dialogService:DialogService,
          public userSessionService:UserSessionService,
          public state:StateService,
          public transition:Transition) {
      this.feedback.companyId=this.transition.params().companyId;
      this.feedback.userId=this.userSessionService.getLoggedInUser()?this.userSessionService.getLoggedInUser().id:null;
      let year=new Date().getFullYear();
      for(let i=30;i>0;i--){
        this.years.push(year--);
      }
  }

  ngOnInit() {
      this.companyService.getCompanyById(this.feedback.companyId).subscribe((company:Company)=>{
        this.company=company;
        this.benefitService.getAllBenefits().subscribe((res:CompanyBenefit[])=>{
          this.feedback.benefits=res;
          for(let i in this.feedback.benefits)
            this.feedback.benefits[i].rating=0;
          this.isLoaded=true;
        });
      });
  }

  submit():void{
    console.log("ReviewComponent.submit",this.feedback);
    if(this.validate()){
      this.disableSave=true;
      if(this.noBenefitsReviewed()){
        this.showNoBenefitReviewedDialogue();
      }else if(this.allBenefitsReviewed()){
        this.save();
      }else{
        this.showFeedbackRevisitDialogue()
      }
      /*this.allBenefitsReviewed()?this.save():this.showFeedbackRevisitDialogue();*/
    }else{
      this.scrollFormElement();
    }
  }

  isLastOddBenefit(index:number):boolean{
    if(this.feedback.benefits && this.feedback.benefits.length && (index == this.feedback.benefits.length-1) && index%2==0)
      return true;
    else
      return false;
  }

  validate():boolean{
    let isValid:boolean =true;
    if(!this.feedback.userAssociationYear){
      this.errorMap.userAssociationYear=true
      isValid=false;
    }
    /*if(!this.feedback.userDesignation){
      this.errorMap.userDesignation=true
      isValid=false;
    }*/
    if(!this.feedback.userMartialStatus){
      this.errorMap.userMartialStatus=true
      isValid=false;
    }
    return isValid;
  }

  showFeedbackThanksDialogue() {
    if(this.isThanksDiaologOpen)
      return;
    this.isThanksDiaologOpen=true;
    let disposable = this.dialogService.addDialog(FeedbackThanksDialogComponent)
      .subscribe((isConfirmed)=>{
        this.isThanksDiaologOpen=false;
        window.scroll(0,0);
        this.state.go("app.user");
      });
  }

  showFeedbackRevisitDialogue() {
    if(this.isRevisitDiaologOpen)
      return;
    this.isRevisitDiaologOpen=true;
    let disposable = this.dialogService.addDialog(FeedbackRevisitDialogComponent)
      .subscribe((save)=>{
        this.isRevisitDiaologOpen=false;
        if(save){
          this.save();
        }else{
          this.disableSave=false;
        }
      });
  }

  showNoBenefitReviewedDialogue() {
    if(this.isNoBenefitReviewedDiaologOpen)
      return;
    this.isNoBenefitReviewedDiaologOpen=true;
    let disposable = this.dialogService.addDialog(NoBenefitReviewedDialogComponent)
      .subscribe((confirm)=>{
        this.isNoBenefitReviewedDiaologOpen=false;
        this.disableSave=false;
      });
  }

  private allBenefitsReviewed():boolean{
    for(let i in this.feedback.benefits){
      if(this.feedback.benefits[i].rating==0){
        return false;
      }
    }
    return true;
  }

  private noBenefitsReviewed():boolean{
    for(let i in this.feedback.benefits){
      if(this.feedback.benefits[i].rating!=0){
        return false;
      }
    }
    return true;
  }

  private save(){
    this.feedbackService.create(this.feedback).subscribe((res:Object)=>{
      this.showFeedbackThanksDialogue();
    });
  }

  private scrollFormElement(){
    let headerHeight=Util.getHeaderHeight();
    window.scroll(0,Util.findElementPosition(document.getElementById("user-designation-input"))-(headerHeight*1.5));
  }


}
