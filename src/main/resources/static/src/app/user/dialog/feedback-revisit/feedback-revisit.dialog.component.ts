import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {StateService} from "@uirouter/angular";
@Component({
  selector: 'feedback-revisit-dialog',
  templateUrl: './feedback-revisit.dialog.component.html'

})
export class FeedbackRevisitDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  confirm(confirm:boolean) {
    this.result = confirm;
    this.close();
  }

}
