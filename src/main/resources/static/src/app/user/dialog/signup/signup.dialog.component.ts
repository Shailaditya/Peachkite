import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {RegistrationService,UserSessionService} from "../../../service/index";

@Component({
  selector: 'signup-dialog',
  templateUrl: './signup.dialog.component.html',
  styleUrls: ['./signup.dialog.component.css']

})

export class SignupDialogComponent extends DialogComponent<void, boolean>{
  user:RegistrationModel=<RegistrationModel>{};
  isTNCAccepted:boolean;
  isRequestSent:boolean=false;
  errorMessage:string;
  invalidEmail:boolean=false;
  constructor(dialogService: DialogService,
              public userSessionService: UserSessionService,
              public registrationService: RegistrationService) {
    super(dialogService);
  }
  cancel() {
    // we set dialog result as true on click on confirm button,
    // then we can get dialog result from caller code
    this.result = false;
    this.close();
  }
  signup(){
    this.errorMessage=null;
    if(!this.isRequestSent && this.isTNCAccepted && this.validateEmail()){
      this.isRequestSent=true;
      this.registrationService.register(this.user).subscribe((res:any)=>{
        this.result = true;
        this.userSessionService.setLoggedInUser(res.body);
        this.close();
      },error=>{
        this.errorMessage=error.status == 409?"Email is already registered.":"Server encountered an unexpected error.";
      });
    }
  }

  validateEmail():boolean {
    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    this.invalidEmail= re.test(this.user.email)?false:true;
    return !this.invalidEmail;
  }

}

export interface RegistrationModel{
  password: string;
  email:string;
  martialStatus:string;
}
