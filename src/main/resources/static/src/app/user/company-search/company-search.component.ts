import {Component, HostListener, OnInit} from '@angular/core';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import {StateService, Transition} from "@uirouter/angular";
import {DialogService} from "ng2-bootstrap-modal";
import {CompanSearchNoResultDialogComponent} from "../dialog/company-search-noresult/company-search-no-result.dialog.component";
import {HttpClient} from "@angular/common/http";
import {Util} from "../../util";
import {debounce} from "rxjs/operator/debounce";
import {SigninDialogComponent} from "../dialog/signin/signin.dialog.component";
import {UserSessionService} from "../../service/user.session.service";
import {CompanyService} from "../../service/company.service";

@Component({
  selector: 'app-company-search',
  templateUrl: './company-search.component.html',
  styleUrls: ['./company-search.component.css']
})
export class CompanySearchComponent implements OnInit {

  company:any;
  isDiaologOpen:boolean=false;
  hideSearch:boolean=false;
  isSigninDiaologOpen:boolean=false;
  searchInputPos:number;
  observableSource = (keyword: any): Observable<any[]> => {
    let url: string =
      '/api/public/company/search?key='+keyword
    if (keyword) {
      return this.http.get(url, {observe: 'response'})
        .map(res => {
          return <any[]>res.body;
        })
    } else {
      return Observable.of([]);
    }
  }

  constructor(public http: HttpClient,
              public state:StateService,
              public userSessionService:UserSessionService,
              public companyService:CompanyService,
              private dialogService:DialogService) {}

  ngOnInit() {
    this.searchInputPos=Util.findElementPosition(document.getElementById("company-search-input"));
  }

  searchSubmit(event:any){
    event.stopImmediatePropagation();
    console.log(this.company);
    if(this.company.id){
      this.gotoCompanyPage();
    }else
      this.showCompanyNotFoundDialog();
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    if((1.33*Util.getHeaderHeight())+window.scrollY > this.searchInputPos){
      this.hideSearch=true;
    }else{
      this.hideSearch=false;
    }
  }

  showCompanyNotFoundDialog() {
    if(this.isDiaologOpen)
      return;
    this.isDiaologOpen=true;
    let disposable = this.dialogService.addDialog(CompanSearchNoResultDialogComponent)
      .subscribe((data:string)=>{
        this.isDiaologOpen=false;
        if(data != null)
          this.companyService.saveMissingCompany(data).subscribe();
      });
  }

  showSigninDialog() {
    if(this.isSigninDiaologOpen)
      return;
    this.isSigninDiaologOpen=true;
    let disposable = this.dialogService.addDialog(SigninDialogComponent,{title:'Sign In to continue.'})
      .subscribe((isConfirmed)=>{
        this.isSigninDiaologOpen=false;
        if(isConfirmed){
          this.gotoCompanyPage();
        }
      });
  }

  private gotoCompanyPage(){
    this.state.go("app.company-detail",{companyId:this.company.id});
  }
}
