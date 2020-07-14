import { Component, OnInit } from '@angular/core';
import {StateService, Transition} from "@uirouter/angular";
import {UserSessionService} from "../../service/user.session.service";
import {CompanyService} from "../../service/company.service";
import {Company} from "../../model/company";

@Component({
  selector: 'app-company-admin-home',
  templateUrl: './company-admin-home.component.html',
  styleUrls: ['./company-admin-home.component.css']
})
export class CompanyAdminHomeComponent implements OnInit {

  isLoaded:boolean=false;

  constructor(public stateService:StateService,
              public userSessionService:UserSessionService,
              public companyService:CompanyService) { }

  ngOnInit() {
    if(this.userSessionService.getLoggedInUser().companyId)
      this.companyService.getCompanyById(this.userSessionService.getLoggedInUser().companyId).subscribe((company:Company)=>{
        this.userSessionService.setCompany(company);
        this.isLoaded=true;
    });
  }

  setActive(event:any,state:string){
    if(document.getElementsByClassName("activeLA").item(0))
      document.getElementsByClassName("activeLA").item(0).classList.remove("activeLA");

    let target:any= event.target || event.srcElement || event.currentTarget;
    target.classList.add("activeLA");
    if(state)
      this.stateService.go(state);
  }

}
