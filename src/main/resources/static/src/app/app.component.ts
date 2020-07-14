import {Component, OnInit} from '@angular/core';
import {UserSessionService} from "./service/index";
import {User} from "./model/index";
import { StateService } from "@uirouter/angular";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  isLoaded:boolean=false;
  constructor(public userService: UserSessionService,public state:StateService,) {

  }

  ngOnInit(){
    this.userService.getExistingUserSession().subscribe((user:User)=>{
      if(user){
        this.userService.setLoggedInUser(user);
      }
      if(this.userService.isAdmin()){
        this.state.go('app.admin-home');
      }if(this.userService.isCompanyAdmin()){
        this.state.go('app.company-admin.general');
      }/*else {
        this.state.go('app.user');
      }*/
      this.isLoaded=true;
    });
  }
}
