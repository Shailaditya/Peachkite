import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UIRouterModule } from "@uirouter/angular";
import { COMPANY_ADMIN_STATES } from './company-admin.states';
import { NguiAutoCompleteModule } from '@ngui/auto-complete';
import {CompanyAdminHomeComponent} from "./company-admin-home/company-admin-home.component";
import {CompanyAdminHeaderComponent} from "./company-admin-header/company-admin-header.component";
import {CompanyGeneralComponent} from "./company-general/company-general.component";
import {CompanyBenefitsComponent} from "./company-benefits/company-benefits.component";
import {CompanyMediaComponent} from "./company-media/company-media.component";
import {PaginationComponent} from "../shared/pagination/pagination.component";
import {CompanyMediaEditComponent} from "./company-media/company-media.edit.component";
import {DpDatePickerModule} from 'ng2-date-picker';
import {CompanyFaqComponent} from "./company-faq/company-faq.component";
import {CompanyFaqEditComponent} from "./company-faq/company-faq.edit.component";
import {FaqLimitErrorDialogComponent} from "./dialog/faq-limit-error/faq-limit-error.dialog.component";
import {NotificationComponent} from "./notification/notification.component";
import {NotificationQueryComponent} from "./notification/notification.query.component";
import {NotificationReviewComponent} from "./notification/notification.review.component";
import {SaveSuccessDialogComponent} from "./dialog/save-success/save-success.dialog.component";
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NguiAutoCompleteModule,
    DpDatePickerModule,
    UIRouterModule.forChild({ states:COMPANY_ADMIN_STATES})
  ],
  declarations: [CompanyAdminHomeComponent,
    FaqLimitErrorDialogComponent,
    SaveSuccessDialogComponent,
    CompanyAdminHeaderComponent,
    CompanyBenefitsComponent,
    CompanyMediaComponent,
    CompanyMediaEditComponent,
    PaginationComponent,
    CompanyFaqComponent,
    CompanyFaqEditComponent,
    NotificationComponent,
    NotificationQueryComponent,
    NotificationReviewComponent,
    CompanyGeneralComponent],
  entryComponents: [FaqLimitErrorDialogComponent,SaveSuccessDialogComponent]
})
export class CompanyAdminModule { }
