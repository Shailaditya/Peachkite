import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
@Component({
  selector: 'save-success-dialog',
  templateUrl: './save-success.dialog.component.html'

})
export class SaveSuccessDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  ok() {
    this.result = true;
    this.close();
  }

}
