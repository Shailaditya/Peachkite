import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {StateService} from "@uirouter/angular";
@Component({
  selector: 'feedback-thanks-dialog',
  templateUrl: './feedback-thanks.dialog.component.html'

})
export class FeedbackThanksDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  ok() {
    this.result = true;
    this.close();
  }

}
