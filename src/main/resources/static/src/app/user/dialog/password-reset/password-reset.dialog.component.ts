import {Component, OnInit} from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {RegistrationService,UserSessionService} from "../../../service/index";
import {HttpClient} from "@angular/common/http";
import {ResetPasswordService} from "../../../service/reset.password.service";

@Component({
  selector: 'password-reset-dialog',
  templateUrl: './password-reset.dialog.component.html',
  styleUrls: ['./password-reset.dialog.component.css']

})

export class PasswordResetDialogComponent extends DialogComponent<any, boolean> implements OnInit {

  invalidPassword:boolean=false;
  invalidConfirmPassword:boolean=false;
  password:string;
  confirmPassword:string;
  success:boolean;
  error:boolean;
  token:string;
  invalidToken:boolean=false;
  isLoaded:boolean=false;
  constructor(public dialogService: DialogService,
              public resetPasswordService: ResetPasswordService,
              public http: HttpClient,
              public registrationService: RegistrationService) {
    super(dialogService);
  }

  ngOnInit() {
    console.log(this.token);
    this.resetPasswordService.checkToken(this.token).subscribe(res=>this.isLoaded=true,error2 => this.invalidToken=true);
  }

  submit(){
    this.resetPasswordService.reset(this.password,this.token).subscribe(res=>this.success=true,error2 => this.error=true);
  }

  validate():boolean {
    this.invalidPassword=false;
    this.invalidConfirmPassword=false;
    if(this.password != null &&  this.password.length >= 6 && this.confirmPassword == this.password)
      return true;
    else if(this.password != null && this.password.length <6){
      this.invalidPassword=true;
      return false;
    }
    else if(this.confirmPassword != this.password){
      this.invalidConfirmPassword=true;
      return false;
    }
  }

  cancel() {
    this.close();
  }

}
