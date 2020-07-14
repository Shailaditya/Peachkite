import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Content} from "../model/content";
import {UserSessionService} from "./user.session.service";

@Injectable()
export class ContentService {
  private adminBaseUrl: string = '/api/admin/content';
  private userBaseUrl: string = '/api/secure/content';
  private publicBaseUrl: string = '/api/public/content';
  constructor(public http: HttpClient,
              public userSessionService: UserSessionService) {
  }

  public getCompanyContentForAdmin(companyId:string,pageNumber:number,pageSize:number): Observable<any> {
    return this.http
      .get<any>(this.adminBaseUrl+'?companyId='+companyId+'&pageNumber='+pageNumber+"&pageSize="+pageSize)
      .catch((error: HttpErrorResponse) => ContentService.handleError(error));
  }

  public getCompanyContentByIdForAdmin(contentId:string): Observable<Content> {
    return this.http
      .get<Content>(this.adminBaseUrl+'/'+contentId)
      .catch((error: HttpErrorResponse) => ContentService.handleError(error));
  }

  public getCompanyContentForUser(companyId:string,pageNumber:number,pageSize:number): Observable<any> {
    let baseurl=this.userSessionService.isLoggedIn()?this.userBaseUrl:this.publicBaseUrl;
    return this.http
      .get<any>(baseurl+'?companyId='+companyId+'&pageNumber='+pageNumber+"&pageSize="+pageSize)
      .catch((error: HttpErrorResponse) => ContentService.handleError(error));
  }

  public createOrUpdateContent(content:Content): Observable<Content> {
    return content.id?
      this.http
      .post<Content>(this.adminBaseUrl,content)
      .catch((error: HttpErrorResponse) => ContentService.handleError(error)) :
      this.http
        .put<Content>(this.adminBaseUrl+"/"+content.id,content)
        .catch((error: HttpErrorResponse) => ContentService.handleError(error));
  }


  static handleError(error: HttpErrorResponse) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error || 'Server error');
  }
}
