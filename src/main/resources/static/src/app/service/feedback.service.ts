import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {Feedback} from "../model/feedback";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class FeedbackService {
  private baseUrl: string = '/api/secure/feedback';

  constructor(public http: HttpClient) {
  }

  public create(feedback:Feedback): Observable<Object> {
    return this.http
      .post(this.baseUrl,feedback)
      .map((res: Object) => res)
      .catch((error: Object) => FeedbackService.handleError(error));
  }

  public exists(userId:String,companyId:string): Observable<Object> {
    return this.http
      .post(this.baseUrl+"/validate",{userId:userId,companyId:companyId},{observe:'response'});
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}
