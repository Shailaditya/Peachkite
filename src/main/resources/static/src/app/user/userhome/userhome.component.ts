import { Component, OnInit } from '@angular/core';
import { StateService, Transition } from "@uirouter/angular";
import {DialogService} from "ng2-bootstrap-modal";
import {PasswordResetDialogComponent} from "../dialog/password-reset/password-reset.dialog.component";
@Component({
  selector: 'app-userhome',
  templateUrl: './userhome.component.html',
  styleUrls: ['./userhome.component.css']
})
export class UserHomeComponent implements OnInit {

  token:string;
  constructor(public transition:Transition,
              public stateService:StateService,
              public dialogService:DialogService) {
    this.token=this.transition.params().token;
  }

  ngOnInit() {
    if(this.stateService.current.name == 'app.reset-password')
      this.openResetPasswordPopup();
  }

  openResetPasswordPopup(){
    let disposable = this.dialogService.addDialog(PasswordResetDialogComponent,{token:this.token})
      .subscribe((isConfirmed)=>{
        this.stateService.go("app.user");
      });
  }

}
