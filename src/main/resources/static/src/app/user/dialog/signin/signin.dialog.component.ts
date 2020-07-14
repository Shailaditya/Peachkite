import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {AuthenticationService,UserSessionService} from "../../../service/index";

export interface SigninModal{
  title:string;
}

@Component({
  selector: 'signin-dialog',
  templateUrl: './signin.dialog.component.html',
  styleUrls: ['./signin.dialog.component.css']
})
export class SigninDialogComponent extends DialogComponent<SigninModal, boolean>{
  title: string;
  password: string;
  username:string;
  rememberme:boolean=true;
  errorMessage:string;
  forgotPassword:boolean=false;
  constructor(dialogService: DialogService,
              public userSessionService: UserSessionService,
              public authenticationService: AuthenticationService) {
    super(dialogService);
  }
  cancel() {
    this.result = false;
    this.close();
  }
  signin(){
    this.errorMessage=null;
    this.authenticationService.signin(this.username,this.password,this.rememberme).subscribe((res:any)=>{
      this.result = true;
      this.userSessionService.setLoggedInUser(res.body);
      this.close();
    },error=>{
      if(error.status == 404)
        this.errorMessage="User with the given email not found in our records."
      else if(error.status=400)
        this.errorMessage="Incorrect password."
      else
        this.errorMessage="Internal server error."
    });
  }

}

