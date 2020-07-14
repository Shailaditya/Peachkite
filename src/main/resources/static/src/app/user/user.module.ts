import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserHomeComponent} from './userhome/userhome.component';
import {AboutusComponent} from './aboutus/aboutus.component';
import {ContactusComponent} from './contactus/contactus.component';
import {HeaderComponent} from './header/header.component';
import {CompanySearchComponent} from './company-search/company-search.component';
import {ReviewComponent} from './review/review.component';
import {UIRouterModule} from "@uirouter/angular";
import { USER_STATES } from './user.states';
import { NguiAutoCompleteModule } from '@ngui/auto-complete';
import {CompanSearchNoResultDialogComponent} from "./dialog/company-search-noresult/company-search-no-result.dialog.component";
import {SigninDialogComponent} from "./dialog/signin/signin.dialog.component";
import {SignupDialogComponent} from "./dialog/signup/signup.dialog.component";
import {FeedbackThanksDialogComponent} from "./dialog/feeback-thanks/feedback-thanks.dialog.component";
import {FeedbackRevisitDialogComponent} from "./dialog/feedback-revisit/feedback-revisit.dialog.component";
import {NoBenefitReviewedDialogComponent} from "./dialog/no-benefit-reviewed/no-benefit-reviewed.dialog.component";
import {ContactusThankyouDialogComponent} from "./dialog/contactus-thankyou/contactus-thankyou.dialog.component";
import {PasswordResetDialogComponent} from "./dialog/password-reset/password-reset.dialog.component";
import {ResetPasswordComponent} from "./reset-password/reset.password.component";
import {CompanyDetailComponent} from "./company-detail/company-detail.component";
import {CompanyTestimonialsComponent} from "./company-testimonials/company-testimonials.component";
import {CompanyContentComponent} from "./company-content/company-content.component";
import {CompanyClaimDialogComponent} from "./dialog/company-claim/company-claim.dialog.component";
import {CompanyQueryDialogComponent} from "./dialog/company-query/company-query.dialog.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NguiAutoCompleteModule,
    UIRouterModule.forChild({ states:USER_STATES})
  ],
  declarations: [UserHomeComponent,
    CompanySearchComponent,
    CompanyDetailComponent,
    AboutusComponent,
    ContactusComponent,
    ReviewComponent,
    CompanSearchNoResultDialogComponent,
    SigninDialogComponent,
    SignupDialogComponent,
    NoBenefitReviewedDialogComponent,
    FeedbackThanksDialogComponent,
    FeedbackRevisitDialogComponent,
    ContactusThankyouDialogComponent,
    CompanyClaimDialogComponent,
    CompanyTestimonialsComponent,
    CompanyContentComponent,
    HeaderComponent,
    CompanySearchComponent,
    AboutusComponent,
    ContactusComponent,
    ReviewComponent,
    CompanSearchNoResultDialogComponent,
    SigninDialogComponent,
    SignupDialogComponent,
    NoBenefitReviewedDialogComponent,
    FeedbackThanksDialogComponent,
    FeedbackRevisitDialogComponent,
    ContactusThankyouDialogComponent,
    ResetPasswordComponent,
    PasswordResetDialogComponent,
    CompanyQueryDialogComponent,
    HeaderComponent],
  entryComponents: [CompanSearchNoResultDialogComponent,
    SigninDialogComponent,
    SignupDialogComponent,
    PasswordResetDialogComponent,
    FeedbackThanksDialogComponent,
    NoBenefitReviewedDialogComponent,
    FeedbackRevisitDialogComponent,
    CompanyClaimDialogComponent,
    CompanyQueryDialogComponent,
    ContactusThankyouDialogComponent]
})
export class UserModule { }
