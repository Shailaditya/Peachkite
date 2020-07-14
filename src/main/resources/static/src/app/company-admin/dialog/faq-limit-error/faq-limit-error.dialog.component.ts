import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
@Component({
  selector: 'faq-limit-error-dialog',
  templateUrl: './faq-limit-error.dialog.component.html'

})
export class FaqLimitErrorDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
  ok() {
    this.result = true;
    this.close();
  }

}
