import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {StateService} from "@uirouter/angular";
@Component({
  selector: 'no-benefit-reviewed-dialog',
  templateUrl: './no-benefit-reviewed.dialog.component.html'

})
export class NoBenefitReviewedDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  ok() {
    this.close();
  }

}
