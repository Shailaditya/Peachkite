import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/forkJoin';
import 'rxjs/add/operator/toPromise';
import {HttpClient} from "@angular/common/http";

@Injectable()
export class UploadService {

  constructor(public http: HttpClient) {
  }

  public upload(formData:FormData): Observable<string> {
    return this.http
      .post<any>("/api/admin/upload",formData,{observe:'response'})
      .map(res=>res.body.url);
  }


  static handleError(error: Object) {
    console.error("SERVICE ERROR:::", error);
    return Observable.throw(error);
  }
}

