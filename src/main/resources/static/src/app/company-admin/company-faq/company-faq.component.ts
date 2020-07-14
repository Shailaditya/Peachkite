
import {Component, OnInit} from "@angular/core";
import {FaqService} from "../../service/faq.service";
import {UserSessionService} from "../../service/user.session.service";
import {StateService} from "@uirouter/angular";
import {Faq} from "../../model/faq";
@Component({
  selector: 'app-company-faq',
  templateUrl: './company-faq.component.html',
  styleUrls: ['./company-faq.component.css']
})
export class CompanyFaqComponent implements OnInit {

  companyId:string;
  faqList:Faq[]=[];

  constructor(public faqService:FaqService,
              public state:StateService,
              public userSessionService:UserSessionService) {
    this.companyId=this.userSessionService.getCompany().id;
  }

  ngOnInit() {
    this.faqService.getCompanyFaqs(this.companyId).subscribe((faqs:Faq[])=>{
      this.faqList=faqs;
    });
  }

  editFaq(faqId:string){
    this.state.go("app.company-admin.faq-edit",{faqId:faqId});
  }

  addFaq(){
    this.state.go("app.company-admin.faq-create");
  }
}
