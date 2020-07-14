import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {StateService} from "@uirouter/angular";
import {SigninDialogComponent} from "../dialog/signin/signin.dialog.component";
import {DialogService} from "ng2-bootstrap-modal";
import {SignupDialogComponent} from "../dialog/signup/signup.dialog.component";
import {UserSessionService} from "../../service/user.session.service";
import {User} from "../../model/user";
import {Subscription} from "rxjs/Subscription";
import {Util} from "../../util";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";
import {CompanSearchNoResultDialogComponent} from "../dialog/company-search-noresult/company-search-no-result.dialog.component";
import {AuthenticationService} from "../../service/authentication.service";
@Component({
  selector: 'user-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit,OnDestroy {

  company:any;
  showSearch:boolean=false;
  showStars:boolean=false;
  isSigninDiaologOpen:boolean=false;
  isSignupDiaologOpen:boolean=false;
  isCompSearchDiaologOpen:boolean=false;
  user:User;
  userSubscription:Subscription;
  sessionSubscription:Subscription;
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
  };
  constructor(public state:StateService,
              public http: HttpClient,
              public userSessionService:UserSessionService,
              public authService:AuthenticationService,
              public dialogService:DialogService) {
    this.user=this.userSessionService.getLoggedInUser();
    this.userSubscription=this.userSessionService.getUserObservable().subscribe((user:User)=>{
      this.user=user;
    });

    this.sessionSubscription=this.userSessionService.getSessionExpiredObservable().subscribe(res=>{
      this.showSigninDialog();
    });

  }

  ngOnInit() {
      if(this.state.current.name == 'app.user'){
        this.showStars=true;
      }else{
        this.showSearch=true;
      }
  }

  @HostListener("window:scroll", [])
  onWindowScroll() {
    if(this.state.current.name == 'app.user'){
      if(Util.findElementPosition(document.getElementById("company-search-input"))){
        this.searchInputPos=Util.findElementPosition(document.getElementById("company-search-input"));
      }
      if((1.33*Util.getHeaderHeight())+window.scrollY > this.searchInputPos){
        this.showSearch=true;
      }else{
        this.showSearch=false;
      }
    }
    /*console.log("Scrolling",
      (1.33*Util.getHeaderHeight())+window.scrollY,
      this.searchInputPos,
      document.body.scrollHeight,
      window.scrollY);*/
  }

  showSigninDialog(title?:string) {
    if(this.isSigninDiaologOpen)
      return;
    this.isSigninDiaologOpen=true;
    let disposable = this.dialogService.addDialog(SigninDialogComponent,{title:(title?title:'Sign - In')}/*, {
      title:'',
      message:''}*/)
      .subscribe((isConfirmed)=>{
        //We get dialog result
        /*if(isConfirmed) {
          alert('accepted');
        }
        else {
          alert('declined');
        }*/
        this.isSigninDiaologOpen=false;
        if(isConfirmed){
          this.state.reload("app");
          /*if(title){
            this.gotoCompanyPage();
          }else{
              this.state.go("app.user");
          }*/
        }
      });
    //We can close dialog calling disposable.unsubscribe();
    //If dialog was not closed manually close it by timeout
    /*setTimeout(()=>{
      disposable.unsubscribe();
    },10000);*/
  }

  showSignupDialog() {
    if(this.isSignupDiaologOpen)
      return;
    this.isSignupDiaologOpen=true;
    let disposable = this.dialogService.addDialog(SignupDialogComponent)
      .subscribe((isConfirmed)=>{
        this.isSignupDiaologOpen=false;
        if(isConfirmed){
          this.state.go("app.user");
        }
      });
  }

  logout(){
    console.log("logging out");
    this.authService.signout().subscribe(res=>{
      this.userSessionService.setLoggedInUser(null);
      if(this.state.current.name == 'app.user'){
        this.state.reload();
      }else{
        this.state.go('app.user');
      }
    });
  }

  searchSubmit(event:any){
    event.stopImmediatePropagation();
    console.log(this.company);
    if(this.company.id)
      this.gotoCompanyPage();
    else
      this.showCompanySearchDialog();
  }

  showCompanySearchDialog() {
    if(this.isCompSearchDiaologOpen)
      return;
    this.isCompSearchDiaologOpen=true;
    let disposable = this.dialogService.addDialog(CompanSearchNoResultDialogComponent)
      .subscribe((isConfirmed)=>{
        this.isCompSearchDiaologOpen=false;
      });
  }

  ngOnDestroy(){
    if(this.userSubscription)
      this.userSubscription.unsubscribe();
    if(this.sessionSubscription)
      this.sessionSubscription.unsubscribe();
  }

  private gotoCompanyPage(){
    this.state.go("app.company-detail",{companyId:this.company.id});
  }

}
