import {Component, OnInit} from "@angular/core";
import {StateService} from "@uirouter/angular";
import {Observable} from "rxjs/Observable";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'admin-home',
  templateUrl: './admin.home.component.html',
  styleUrls: ['./admin.home.component.css']
})
export class AdminHomeComponent implements OnInit {

  company:any;

  observableSource = (keyword: any): Observable<any[]> => {
    let url: string =
      '/api/public/company/search?key='+keyword
    if (keyword) {
      return this.http.get(url, {observe: 'response'})
        .map(res => {
          return <any[]>res.body;
        })
    } else {
      return Observable.of([]);
    }
  }

  constructor(public http: HttpClient,
              public state:StateService) {}

  ngOnInit() {
  }

  searchSubmit(event:any){
    event.stopImmediatePropagation();
    console.log(this.company);
    if(this.company.id){
      this.gotoCompanyPage();
    }
  }

  addNewCompany(){
    this.state.go("app.admin-company-add");
  }

  private gotoCompanyPage(){
    this.state.go("app.admin-company-edit",{companyId:this.company.id});
  }

}
