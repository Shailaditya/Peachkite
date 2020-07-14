import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";
import {CompanyBenefit} from "../model/company";

@Injectable()
export class BenefitService {
    private baseUrl: string = '/api/secure/benefit';

    constructor(public http: HttpClient) {
    }

    public getAllBenefits(): Observable<Object> {
        return this.http
            .get<CompanyBenefit[]>(this.baseUrl)
            .map((res: Object) => res)
            .catch((error: Object) => BenefitService.handleError(error));
    }


    static handleError(error: Object) {
        console.error("SERVICE ERROR:::", error);
        return Observable.throw(error || 'Server error');
    }
}
