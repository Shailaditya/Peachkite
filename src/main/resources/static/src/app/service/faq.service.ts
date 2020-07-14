import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Faq} from "../model/faq";

@Injectable()
export class FaqService {
  private basePublicUrl: string = '/api/public/faq';
  private baseAdminUrl: string = '/api/admin/faq';

  constructor(public http: HttpClient) {
  }

  public getPublishedFaqs(companyId:string): Observable<Object> {
    return this.http
      .get<Faq[]>(this.basePublicUrl+'?companyId='+companyId+'&status=Published')
      .map((res: Object) => res)
      .catch((error: Object) => FaqService.handleError(error));
  }

  public getCompanyFaqs(companyId:string): Observable<Faq[]> {
    return this.http
      .get<Faq[]>(this.baseAdminUrl+'?companyId='+companyId)
      .map((res: Object) => res)
      .catch((error: Object) => FaqService.handleError(error));
  }

  public getFaqById(faqId:string): Observable<Faq> {
    return this.http
      .get<Faq>(this.baseAdminUrl+'/'+faqId)
      .map((res: Object) => res)
      .catch((error: Object) => FaqService.handleError(error));
  }

  public saveFaq(faq:Faq): Observable<HttpResponse<any>> {
    return faq.id?this.http
      .put(this.baseAdminUrl+'/'+faq.id,faq,{observe:'response'})
      .catch((error: Object) => FaqService.handleError(error)):
      this.http
        .post(this.baseAdminUrl,faq,{observe:'response'})
        .catch((error: Object) => FaqService.handleError(error));
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error || 'Server error');
  }
}
