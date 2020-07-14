import {Injectable, Injector} from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse,
  HttpErrorResponse
} from '@angular/common/http';
import {Observable} from "rxjs/Observable";
import 'rxjs/add/operator/do';
import {StateService, Transition} from "@uirouter/angular";
import {UserSessionService} from "./service/index";
import {DialogService} from "ng2-bootstrap-modal";
import {SigninDialogComponent} from "./user/dialog/signin/signin.dialog.component";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  private userSessionService:UserSessionService;
  private isSigninDiaologOpen:boolean=false;
  constructor(public state: StateService,
              private dialogService:DialogService,
              private injector: Injector) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if(!this.userSessionService){
      this.userSessionService=this.injector.get(UserSessionService);
    }
    return next.handle(req).do(event => {}, err => {
      if (err instanceof HttpErrorResponse && err.status == 403) {
        this.showSigninDialog()
      }
    });
  }

  showSigninDialog() {
    if(this.isSigninDiaologOpen)
      return;
    this.isSigninDiaologOpen=true;
    let disposable = this.dialogService.addDialog(SigninDialogComponent,{title:'Session expired please sign In to continue.'}/*, {
      title:'',
      message:''}*/)
      .subscribe((isConfirmed)=>{
        this.isSigninDiaologOpen=false;
      });
  }
}
