import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ResetPasswordService} from "../../service/reset.password.service";

@Component({
  selector: 'reset-password',
  templateUrl: './reset.password.component.html',
  styleUrls: ['./reset.password.component.css']
})

export class ResetPasswordComponent  implements OnInit{

  @Output()
  close:EventEmitter<void> = new EventEmitter();
  requestSent:boolean=false;
  email:string;
  errorMessage:string;
  block:boolean=false;
  constructor(public resetPasswordService:ResetPasswordService) {

  }

  ngOnInit() {

  }

  reset(){
    this.block=true;
    this.resetPasswordService.generateRequest(this.email).subscribe(res=>{
            // this.block=false;
            this.requestSent=true;
            },
        error2 => {
      this.block=false;
      if(error2.statusCode==404)
        this.errorMessage="This email id is not registered with us.";
      else
        this.errorMessage="Error occurred while processing your request.";
    });
  }

  validate():boolean{
    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(this.email)?true:false;
  }

  ok(){
    this.close.emit();
  }
}

