import {Component, OnInit} from "@angular/core";
import {UserSessionService} from "../../service/user.session.service";
import {AuthenticationService} from "../../service/authentication.service";
import {StateService} from "@uirouter/angular";

@Component({
  selector: 'admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.css']
})
export class AdminHeaderComponent implements OnInit {

  username:string;
  constructor(public userSessionService:UserSessionService,
              public authService:AuthenticationService,
              public state:StateService) {

  }

  ngOnInit() {
    this.username=this.userSessionService.getLoggedInUser().email;
  }

  logout(){
    console.log("logging out");
    this.authService.signout().subscribe(res=>{
      this.userSessionService.setLoggedInUser(null);
      this.state.go('app.user');
    });
  }


}
