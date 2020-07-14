import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";
import {Company, CompanyBenefit} from "../model/company";
import {UserSessionService} from "./user.session.service";

@Injectable()
export class CompanyService {

  private userBaseUrl: string = '/api/secure/company';
  private adminBaseUrl: string = '/api/admin/company';
  private superAdminBaseUrl: string = '/api/super_admin/company';
  private publicBaseUrl: string = '/api/public/company';
  constructor(public http: HttpClient,
              public userSessionService:UserSessionService) {
  }

  public getCompanyById(compnayId:string): Observable<Company> {
    return this.http
      .get<Company>(this.publicBaseUrl + "/"+compnayId)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public sendQuery(companyId:string,query:string): Observable<Object> {
    return this.http
      .post<Object>(this.userBaseUrl + "/"+companyId+'/query',query)
      .map((res: Object) => res)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public viewCompany(companyId:string): Observable<Object> {
    return this.http
      .post<Object>(this.userBaseUrl + "/"+companyId+'/view',null)
      .map((res: Object) => res)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public getTestimonials(companyId:string,pageNumber:number,pageSize:number): Observable<Object> {
    let baseUrl=this.userSessionService.isLoggedIn()?this.userBaseUrl:this.publicBaseUrl;
    return this.http
      .get<Object>(baseUrl + "/"+companyId+'/review?pageNumber='+pageNumber+'&pageSize='+pageSize)
      .map((res: Object) => res)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public save(company:Company): Observable<Company> {
    return company.id?this.http
      .put<Company>(this.adminBaseUrl + "/"+company.id,company)
      .catch((error: Object) => CompanyService.handleError(error)):
      this.http
        .post<Company>(this.superAdminBaseUrl,company)
        .catch((error: Object) => CompanyService.handleError(error))
      ;
  }

  public saveBenefits(companyId:string,companyBenefits:CompanyBenefit[]): Observable<CompanyBenefit[]> {
    return this.http
      .post<CompanyBenefit[]>(this.adminBaseUrl + "/"+companyId+"/benefit",companyBenefits)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public claimCompany(companyId:string,name:string,email:string): Observable<void> {
    return this.http
      .post<void>('/api/public/claim_company',{companyId:companyId,companyName:name,email:email})
      .catch((error: Object) => CompanyService.handleError(error));
  }

  public saveMissingCompany(data:any): Observable<Object> {
    return this.http
      .post<any>("/api/public/company/missing",data)
      .map((res: Object) => res)
      .catch((error: Object) => CompanyService.handleError(error));
  }

  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error || 'Server error');
  }
}
