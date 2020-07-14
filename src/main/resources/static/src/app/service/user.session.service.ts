import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/index";
import {Subject} from "rxjs/Subject";
import {Util} from "../util";
import {Company} from "../model/company";

@Injectable()
export class UserSessionService {

  private user:User;

  private company:Company;

  private userSubject:Subject<User>=new Subject<User>();

  private userObservable:Observable<User>=this.userSubject.asObservable();

  private sessionExpiredSubject:Subject<void>=new Subject<void>();

  private sessionExpiredObservable:Observable<void>=this.sessionExpiredSubject.asObservable();

  constructor(public http: HttpClient) {
  }

  public getExistingUserSession(): Observable<Object> {
    return this.http
      .get<User>("/api/public/currentUser")
      .map((res: Object) => res)
      .catch((error: Object) => UserSessionService.handleError(error));
  }

  public isLoggedIn():boolean{
    return this.user != null;
  }

  public getLoggedInUser():User{
    return this.user;
  }

  public setLoggedInUser(user:User){
    this.user=user;
    this.userSubject.next(user);
  }

  public getUserObservable():Observable<User>{
    return this.userObservable;
  }

  public getSessionExpiredSubject():Subject<void>{
    return this.sessionExpiredSubject;
  }

  public getSessionExpiredObservable():Observable<void>{
    return this.sessionExpiredObservable;
  }

  public isAdmin():boolean{
    if(this.user && Util.searchArray(this.user.roles,'ROLE_ADMIN'))
      return true;
    else
      return false;
  }

  public isCompanyAdmin():boolean{
    if(this.user && Util.searchArray(this.user.roles,'ROLE_COMPANY_ADMIN'))
      return true;
    else
      return false;
  }

  public getCompany():Company{
    return this.company;
  }

  public setCompany(company:Company):void{
    this.company=company;
  }

  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}
