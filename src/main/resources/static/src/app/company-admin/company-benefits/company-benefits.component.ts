import { Component, OnInit } from '@angular/core';
import {UserSessionService} from "../../service/user.session.service";
import {CompanyBenefit} from "../../model/company";
import {CompanyService} from "../../service/company.service";
import {SaveSuccessDialogComponent} from "../dialog/save-success/save-success.dialog.component";
import {DialogService} from "ng2-bootstrap-modal";

@Component({
  selector: 'app-company-benefits',
  templateUrl: './company-benefits.component.html',
  styleUrls: ['./company-benefits.component.css']
})
export class CompanyBenefitsComponent implements OnInit {

  private companyId:string;
  standardBenefits:CompanyBenefit[][]=[];
  companyBenefits:CompanyBenefit[]=[];

  disableSave:boolean=false;
  constructor(private userSessionService:UserSessionService,
              public dialogService:DialogService,
              private companyService:CompanyService) {
  }

  ngOnInit() {
    let benefits:CompanyBenefit[] =this.userSessionService.getCompany().benefits;
    this.companyId=this.userSessionService.getCompany().id;
    this.splitBenefits(benefits);
  }

  addBenefit(){
    if(this.companyBenefits.length == 5)
      return;
    this.companyBenefits.push(<CompanyBenefit>{type:2,isSelected:true});
  }

  deleteBenefit(index:number){
    if(this.companyBenefits.length == 1)
      this.companyBenefits=[<CompanyBenefit>{type:2,isSelected:true}];
    else
      this.companyBenefits.splice(index, 1)
  }

  save(){
    if(this.disableSave)
      return;

    this.disableSave=true;
    this.companyService.saveBenefits(this.companyId,this.mergeBenefits()).subscribe((benefits:CompanyBenefit[])=>{
      this.userSessionService.getCompany().benefits=benefits;
      this.splitBenefits(benefits);
      this.disableSave=false;
      this.dialogService.addDialog(SaveSuccessDialogComponent).subscribe();
    },error2 => this.disableSave=false);
  }

  private mergeBenefits():CompanyBenefit[]{
    let benefits:CompanyBenefit[]=[];
    for(let index in this.standardBenefits){
      benefits=benefits.concat(this.standardBenefits[index]);
    }
    /*console.log(this.companyBenefits);
    console.log(this.companyBenefits.filter((benefit:CompanyBenefit)=>{
      !(benefit.label && benefit.label.trim() != '');
    }));
    console.log(this.companyBenefits);*/
    return benefits.concat(this.companyBenefits.filter((benefit:CompanyBenefit)=>{
      return (benefit.label && benefit.label.trim() != '');
    }));
  }

  private splitBenefits(benefits:CompanyBenefit[]){
    this.standardBenefits=[];
    this.companyBenefits=[];
    let filtered:CompanyBenefit[]=benefits.filter((benefit:CompanyBenefit)=>{
      return benefit.type == 1;
    });

    while(filtered.length>=2)
      this.standardBenefits.push(filtered.splice(0,2));

    this.companyBenefits=benefits.filter((benefit:CompanyBenefit)=>{
      return benefit.type == 2;
    });

    this.companyBenefits=this.companyBenefits.length == 0?[<CompanyBenefit>{type:2,isSelected:true}]:this.companyBenefits;
  }

}
