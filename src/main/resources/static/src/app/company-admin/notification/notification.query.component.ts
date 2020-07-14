import { Component, OnInit } from '@angular/core';
import {StateService,Transition} from "@uirouter/angular";
import * as moment from 'moment';
import {NotificationService} from "../../service/notification.service";
import {Notification} from "../../model/notification";
import {SaveSuccessDialogComponent} from "../dialog/save-success/save-success.dialog.component";
import {DialogService} from "ng2-bootstrap-modal";
@Component({
  selector: 'app-notification-query',
  templateUrl: './notification.query.component.html',
  styleUrls: ['./notification.query.component.css']
})
export class NotificationQueryComponent implements OnInit {
  notificationId:string;
  notification:Notification;
  query:string;
  response:string;
  disableSave:boolean=true;
  constructor(public notificationService:NotificationService,
              public state:StateService,
              public dialogService:DialogService,
              public transition:Transition) {
    this.notificationId=this.transition.params().notificationId;
  }

  ngOnInit() {
    this.notificationService.getNotification(this.notificationId).subscribe(
      (res:Notification)=>{
        this.notification=res;
        this.query=this.notification.detail;
        this.disableSave=false;
      }
    );
  }

  goto(st:string){
    this.state.go(st);
  }

  send(){
    if(this.disableSave || !this.response)
      return;
    this.disableSave=true;
    this.notificationService.reply(this.notificationId,this.response).subscribe(res=>{
      this.dialogService.addDialog(SaveSuccessDialogComponent).subscribe(a=>this.state.go("app.company-admin.notifications"));
    },error2 => this.disableSave=false);
  }

}
