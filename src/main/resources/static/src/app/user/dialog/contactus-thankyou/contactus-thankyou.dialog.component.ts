import { Component } from '@angular/core';
import { DialogComponent, DialogService } from "ng2-bootstrap-modal";
import {RegistrationService,UserSessionService} from "../../../service/index";
@Component({
  selector: 'contactus-thankyou',
  templateUrl: './contactus-thankyou.dialog.component.html',
  styleUrls: ['./contactus-thankyou.dialog.component.css']

})
export class ContactusThankyouDialogComponent extends DialogComponent<void, boolean>{
  constructor(dialogService: DialogService) {
    super(dialogService);
  }
}

