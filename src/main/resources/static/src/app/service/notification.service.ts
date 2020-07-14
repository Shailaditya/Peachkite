import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";
import {Notification} from "../model/notification";
@Injectable()
export class NotificationService {

  private baseUrl:string='/api/admin/notification';
  constructor(public http: HttpClient) {

  }

  public getNotifications(companyId:string,pageNumber:number,pageSize:number): Observable<any> {
    return this.http.get(this.baseUrl+"?companyId="+companyId+"&pageNumber="+pageNumber+"&pageSize="+pageSize)
      .catch((error: Object) => NotificationService.handleError(error));
  }

  public getNotification(notificationId:string): Observable<Notification> {
    return this.http.get(this.baseUrl+"/"+notificationId)
      .catch((error: Object) => NotificationService.handleError(error));
  }

  public reply(notificationId:string,response:string): Observable<any> {
    return this.http.post(this.baseUrl+"/"+notificationId+"/reply",{response:response})
      .catch((error: Object) => NotificationService.handleError(error));
  }

  public getFeedback(feedbackId:string): Observable<any> {
    return this.http.get(this.baseUrl+"/feedback/"+feedbackId)
      .catch((error: Object) => NotificationService.handleError(error));
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}
