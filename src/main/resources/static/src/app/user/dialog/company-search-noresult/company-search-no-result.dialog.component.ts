import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";

@Component({
  selector: 'company-search-no-result-dialog',
  templateUrl: './company-search-no-result.dialog.component.html',
  styleUrls: ['./company-search-no-result.dialog.component.css']

})
export class CompanSearchNoResultDialogComponent extends DialogComponent<void, any>/*<ConfirmModel, boolean> implements ConfirmModel*/ {
  /*title: string;
  message: string;*/
  email:string;
  name:string;
  error:any={};
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm() {
    // we set dialog result as true on click on confirm button,
    // then we can get dialog result from caller code
    this.result = null;
    this.close();
  }
  notify(){
    if(this.validate()){
      console.log(this.email);
      this.result = {email:this.email,name:this.name};
      this.close();
    }
  }

  private validate():boolean {
    let isSuccess:boolean=true;

    if(!this.name){
      isSuccess=false;
      this.error.name=true;
    }else{
      this.error.name=false;
    }

    let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(!this.email || !re.test(this.email)){
      isSuccess=false;
      this.error.email=true;
    }else{
      this.error.email=false;
    }

    return isSuccess;
  }

}
/*
export interface ConfirmModel {
  title:string;
  message:string;
}*/
