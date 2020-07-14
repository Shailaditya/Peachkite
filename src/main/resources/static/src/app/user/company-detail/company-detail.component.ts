import {Component, OnInit} from '@angular/core';
import 'rxjs/add/observable/of';
import {StateService, Transition} from "@uirouter/angular";
import {HttpClient} from "@angular/common/http";
import {CompanyService} from "../../service/company.service";
import {Company, CompanyBenefit} from "../../model/company";
import {FaqService} from "../../service/faq.service";
import {Faq} from "../../model/faq";
import {DialogService} from "ng2-bootstrap-modal";
import {CompanyClaimDialogComponent} from "../dialog/company-claim/company-claim.dialog.component";
import {UserSessionService} from "../../service/user.session.service";
import {SigninDialogComponent} from "../dialog/signin/signin.dialog.component";
import {FeedbackService} from "../../service/feedback.service";
import {Observable} from "rxjs/Observable";
import {CompanyQueryDialogComponent} from "../dialog/company-query/company-query.dialog.component";

@Component({
  selector: 'app-company-detail',
  templateUrl: './company-detail.component.html',
  styleUrls: ['./company-detail.component.css']
})
export class CompanyDetailComponent implements OnInit {

  company:Company;
  companyId:string;
  benefitArrays:CompanyBenefit[][]=[];
  standardBenefitArrays:string[][]=[];
  isLoaded:boolean=false;
  isRated:boolean=true;
  faqs:Faq[]=[];
  isQueryBoxOpen:boolean=false;
  queryText:string;
  querySemaphore:boolean=true;
  isLoggedIn:boolean=false;
  isSigninDiaologOpen:boolean=false;
  constructor(public http: HttpClient,
              public companyService:CompanyService,
              public faqService:FaqService,
              public state:StateService,
              public userSessionService:UserSessionService,
              public feedbackService:FeedbackService,
              public transition:Transition,
              private dialogService:DialogService) {
    this.companyId=this.transition.params().companyId;
    this.isLoggedIn=this.userSessionService.isLoggedIn();
  }

  ngOnInit() {
    this.companyService.getCompanyById(this.companyId).subscribe((company:Company)=>{
      this.company=company;

      let standarBenefits:CompanyBenefit[]=this.company.benefits.filter((benefit:CompanyBenefit)=>{
        return benefit.type ==1;
      });

      let center=standarBenefits.length/2;
      if(standarBenefits.length % 2 != 0){
        center++;
      }
      this.benefitArrays.push(standarBenefits.slice(0, center));
      this.benefitArrays.push(standarBenefits.slice(center));
      console.log("CompanyDetailComponent.ngOnInit benefitArrays",this.benefitArrays);

      standarBenefits=this.company.benefits.filter((benefit:CompanyBenefit)=>{
        return benefit.isSelected;
      });

      let benefitLength=standarBenefits.length;
      let spliceIndex=benefitLength/3;
      if(benefitLength % 3 >=1){
        spliceIndex++;
      }
      this.standardBenefitArrays.push(standarBenefits.splice(0,spliceIndex).map((benefit:CompanyBenefit)=>{
        return benefit.label;
      }));

      spliceIndex=benefitLength/3;
      if(benefitLength % 3 ==2){
        spliceIndex++;
      }
      this.standardBenefitArrays.push(standarBenefits.splice(0,spliceIndex).map((benefit:CompanyBenefit)=>{
        return benefit.label;
      }));

      spliceIndex=benefitLength/3;
      this.standardBenefitArrays.push(standarBenefits.splice(0,spliceIndex).map((benefit:CompanyBenefit)=>{
        return benefit.label;
      }));

      console.log("CompanyDetailComponent.ngOnInit this.standardBenefitArrays",this.standardBenefitArrays);
      this.isLoaded=true;
    });

    this.faqService.getPublishedFaqs(this.companyId).subscribe((faqs:Faq[])=>{
      this.faqs=faqs;
    });

    if(this.userSessionService.isLoggedIn()){
      this.checkRated().subscribe(res=>{
        console.log(res);
        this.isRated=false
      },error2 => {
        this.isRated=true;
      });
    }else{
      this.isRated=false;
    }

    this.companyService.viewCompany(this.companyId).subscribe();
  }

  checkRated():Observable<any>{
    return this.feedbackService.exists(this.userSessionService.getLoggedInUser().id,this.companyId);
  }

  roundOff(val:number){
    return Number(val).toFixed(1)
  }

  sendQuery(){
    if(this.queryText != null && this.queryText != '' && this.querySemaphore){
      this.querySemaphore=false;
      this.companyService.sendQuery(this.companyId,this.queryText).subscribe(res=>{
        this.dialogService.addDialog(CompanyQueryDialogComponent)
          .subscribe((isConfirmed)=>{
            this.isQueryBoxOpen=false;
            this.queryText=null;
            this.querySemaphore=true;
          });
      });
    }
  }

  scrollInView(id:string){
    //105 is header height
    window.scroll(0,document.getElementById(id).offsetTop-105);
  }

  gotoReviewPage(){
    this.state.go("app.review",{companyId:this.company.id});
  }

  claimCompany(){
    this.dialogService.addDialog(CompanyClaimDialogComponent)
      .subscribe((email:string)=>{
        if(email)
          this.companyService.claimCompany(this.companyId,this.company.name,email).subscribe(res=>console.log("Claim accepted"),
            error2 => console.log("Claim error"));
      });
  }

  handleAskNowClick(){
    if(this.userSessionService.isLoggedIn()){
      this.isQueryBoxOpen=true;
    }else{
      this.showSigninDialog();
    }
  }

  public showSigninDialog() {
    if(this.isSigninDiaologOpen)
      return;
    this.isSigninDiaologOpen=true;
    let disposable = this.dialogService.addDialog(SigninDialogComponent,{title:'Please sign In to continue.'})
      .subscribe((isConfirmed)=>{
        this.checkRated().subscribe(res=>{
          console.log(res);
          this.isRated=res.statusCode != 200;
          this.isSigninDiaologOpen=false;
        },error2 => {
          this.isRated=true;
          this.isSigninDiaologOpen=false;
        });
      });
  }
}
