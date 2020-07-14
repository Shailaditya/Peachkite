import { Component, OnInit } from '@angular/core';
import {StateService,Transition} from "@uirouter/angular";
import {NotificationService} from "../../service/notification.service";
@Component({
  selector: 'app-notification-review',
  templateUrl: './notification.review.component.html',
  styleUrls: ['./notification.review.component.css']
})
export class NotificationReviewComponent implements OnInit {
  feedbackId:string;
  feedback:any;
  constructor(public notificationService:NotificationService,
              public state:StateService,
              public transition:Transition) {
    this.feedbackId=this.transition.params().feedbackId;
  }

  ngOnInit() {
    this.notificationService.getFeedback(this.feedbackId).subscribe(
      (res:any)=>{
        this.feedback=res;
      }
    );
  }

  back(){
    this.state.go('app.company-admin.notifications');
  }

  isLastOddBenefit(index:number):boolean{
    if(this.feedback.benefits && this.feedback.benefits.length && (index == this.feedback.benefits.length-1) && index%2==0)
      return true;
    else
      return false;
  }
}
