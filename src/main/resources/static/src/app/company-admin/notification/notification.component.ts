import { Component, OnInit } from '@angular/core';
import {UserSessionService} from "../../service/user.session.service";
import {StateService} from "@uirouter/angular";
import * as moment from 'moment';
import {NotificationService} from "../../service/notification.service";
import {Notification} from "../../model/notification";
@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {
  public static DATE_FORMAT:string='DD-MM-YYYY';
  companyId:string;
  notifications:Notification[]=[];
  totalPages:number=1;
  pageSize:number=5;
  loaded:boolean=false;
  constructor(public notificationService:NotificationService,
              public state:StateService,
              public userSessionService:UserSessionService) {
    this.companyId=this.userSessionService.getCompany().id;
  }

  ngOnInit() {
    this.loadData(0);
  }

  loadData(pageNumber:any){
    console.log("NotificationComponent.loadData","pageNumber",pageNumber);
    this.notificationService.getNotifications(this.companyId,pageNumber,this.pageSize).subscribe(
      res=>{
        this.notifications= <Notification[]>res.content;
        this.totalPages= res.totalPages;
        this.loaded=true;
      }
    );
  }

  viewNotification(notification:Notification){
    if(notification.type=='Query')
      this.state.go("app.company-admin.notification-query",{notificationId:notification.id});
    else
      this.state.go("app.company-admin.notification-review",{feedbackId:notification.feedbackId});
  }

  formatDate(date:Date):string{
    return moment(date).format(NotificationComponent.DATE_FORMAT);
  }
}
