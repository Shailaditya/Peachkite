import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient, HttpResponse} from "@angular/common/http";

@Injectable()
export class AuthenticationService {

  constructor(public http: HttpClient) {
  }

  public signin(username:string,password:string,rememberme?:boolean): Observable<Object> {
    return this.http
      .post("/login",{username:username,password:password,rememberme:rememberme},{observe: 'response'})
      .map((res: any) => res)
      .catch((error: Object) => AuthenticationService.handleError(error));
  }

  public signout(): Observable<Object> {
    return this.http
      .get("/logout")
      .map((res: any) => res)
      .catch((error: Object) => AuthenticationService.handleError(error));
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}
