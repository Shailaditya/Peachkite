import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ContactusService {

  constructor(public http: HttpClient) {
  }

  public sendQuery(query:any): Observable<Object> {
    return this.http
      .post("/api/public/query",query,{observe: 'response'})
      .map((res: Object) => res)
      .catch((error: Object) => ContactusService.handleError(error));
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}
