import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UIRouterModule } from "@uirouter/angular";
import { NguiAutoCompleteModule } from '@ngui/auto-complete';
import {ADMIN_STATES} from "./admin.states";
import {AdminHomeComponent} from "./home/admin.home.component";
import {AdminHeaderComponent} from "app/admin/header/admin-header.component";
import {SaveSuccessDialogComponent} from "app/admin/dialog/save-success/save-success.dialog.component";
import {CompanyEditComponent} from "./company-edit/company-edit.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NguiAutoCompleteModule,
    UIRouterModule.forChild({ states:ADMIN_STATES})
  ],
  declarations: [CompanyEditComponent,SaveSuccessDialogComponent,AdminHomeComponent,AdminHeaderComponent],
  entryComponents: [SaveSuccessDialogComponent]
})
export class AdminModule { }
