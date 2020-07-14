import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";

@Component({
  selector: 'company-claim-dialog',
  templateUrl: './company-claim.dialog.component.html'

})
export class CompanyClaimDialogComponent extends DialogComponent<void, string>{
  email:string;
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  process(){
    if(this.validateEmail()){
      this.result=this.email;
      this.close();
    }
  }

  ignore(){
    this.close();
  }

  validateEmail():boolean {
    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return  re.test(this.email)?true:false;
  }

}
