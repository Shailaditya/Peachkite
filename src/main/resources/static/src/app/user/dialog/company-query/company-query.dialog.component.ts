import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
@Component({
  selector: 'company-query',
  templateUrl: './company-query.dialog.component.html',
  styleUrls: ['./company-query.dialog.component.css']

})
export class CompanyQueryDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
}

