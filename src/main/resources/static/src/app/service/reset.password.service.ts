import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient, HttpResponse} from "@angular/common/http";

@Injectable()
export class ResetPasswordService {

  constructor(public http: HttpClient) {
  }

  public checkToken(token:string ): Observable<Object> {
    return this.http
      .get("/api/public/rp/ct?id="+token)
      .map((res: any) => res)
      .catch((error: Object) => ResetPasswordService.handleError(error));
  }

  public generateRequest(email:string ): Observable<Object> {
    return this.http
      .post("/api/public/rp/generate?email="+email,null)
      .map((res: any) => res)
      .catch((error: Object) => ResetPasswordService.handleError(error));
  }

  public reset(password:string,token:string): Observable<Object> {
    return this.http
      .post("/api/public/rp",{password:password,token:token})
      .map((res: Object) => res)
      .catch((error: Object) => ResetPasswordService.handleError(error));;
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}

