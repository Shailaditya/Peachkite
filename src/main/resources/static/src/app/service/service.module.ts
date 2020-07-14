import { NgModule } from '@angular/core';
import { BenefitService } from './benefit.service';
import { CompanyService } from './company.service';
import { FeedbackService } from './feedback.service';
import {AuthenticationService} from "./authentication.service";
import {RegistrationService} from "./registration.service";
import {UserSessionService} from "./user.session.service";
import {ContactusService} from "./contactus.service";
import {FaqService} from "./faq.service";
import { ContentService} from "./content.service";
import { UploadService} from "./upload.service";
import {ResetPasswordService} from "./reset.password.service";
import {NotificationService} from "./notification.service";
@NgModule({
  imports: [],
  declarations: [],
  exports: [],
  providers: [BenefitService,CompanyService,FeedbackService,
    AuthenticationService,RegistrationService,UserSessionService,
    ResetPasswordService,NotificationService,
    ContactusService,FaqService,ContentService,UploadService]
})
export class ServiceModule {
}
