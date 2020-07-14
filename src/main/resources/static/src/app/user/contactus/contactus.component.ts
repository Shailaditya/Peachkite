import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserSessionService} from "../../service/user.session.service";
import {ContactusService} from "../../service/contactus.service";
import {DialogService} from "ng2-bootstrap-modal";
import {ContactusThankyouDialogComponent} from "../dialog/contactus-thankyou/contactus-thankyou.dialog.component";
import {Subscription} from "rxjs/Subscription";
import {User} from "../../model/user";

@Component({
  selector: 'app-contactus',
  templateUrl: './contactus.component.html',
  styleUrls: ['./contactus.component.css']
})
export class ContactusComponent implements OnInit,OnDestroy {
  isDialogOpen:boolean=false;
  modal:QueryModal=<QueryModal>{};
  userSubscription:Subscription;
  error:any={};
  isLoggedIn:boolean=false;
  allowSubmit:boolean=true;
  constructor(public userSessionService:UserSessionService,
              public contactusService:ContactusService,
              private dialogService:DialogService) { }

  ngOnInit() {
    if(this.userSessionService.getLoggedInUser()){
      this.modal.email=this.userSessionService.getLoggedInUser().email;
      this.isLoggedIn=true;
    }

    this.userSubscription=this.userSessionService.getUserObservable().subscribe((user:User)=>{
      this.modal.email=user?user.email:null;
      this.isLoggedIn=user?true:false;
    });
  }

  submit(){
    if(this.validate() && this.allowSubmit){
      this.allowSubmit=false;
      this.contactusService.sendQuery(this.modal).subscribe((res:Response)=>{
        this.modal=<QueryModal>{};
        this.error={};
        this.showThankYouDialog();
      });
    }
  }

  showThankYouDialog() {
    if(this.isDialogOpen)
      return;
    this.isDialogOpen=true;
    let disposable = this.dialogService.addDialog(ContactusThankyouDialogComponent)
      .subscribe((isConfirmed)=>{
        this.isDialogOpen=false;
        this.allowSubmit=true;
      });
  }

  ngOnDestroy() {
    if (this.userSubscription)
      this.userSubscription.unsubscribe();
  }

  private validate():boolean{
    let isValid:boolean=true;
    this.error={};
    if(!this.modal.email){
      isValid=false;
      this.error.email='* Email is required';
    }else if(this.validateEmail(this.modal.email)){
      isValid=false;
      this.error.email='* Email is not valid';
    }

    if(!this.modal.phone){
      isValid=false;
      this.error.phone='* Contact number is required.'
    }else if(this.modal.phone.toString().length > 11 || isNaN(this.modal.phone)){
      isValid=false;
      this.error.phone='* Invalid contact number.'
    }

    if(!this.modal.name){
      isValid=false;
      this.error.name='* Name is required'
    }else if(this.modal.name.length > 50){
      isValid=false;
      this.error.name='* Name length cannot exceed 50.'
    }

    if(!this.modal.query){
      isValid=false;
      this.error.query='* Query field cannot be is empty.'
    }else if(this.modal.query.toString().length > 1000){
      isValid=false;
      this.error.name='* Query length cannot exceed 1000.'
    }

    return isValid;
  }

  private validateEmail(email:string):boolean {
    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email)?false:true;
  }


}

interface QueryModal{
  name:string;
  email:string;
  phone:number;
  query:string;
}
