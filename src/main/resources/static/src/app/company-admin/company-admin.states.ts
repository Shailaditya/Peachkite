import {Ng2StateDeclaration} from '@uirouter/angular';
import {CompanyAdminHomeComponent} from "./company-admin-home/company-admin-home.component";
import {CompanyAdminHeaderComponent} from "./company-admin-header/company-admin-header.component";
import {CompanyGeneralComponent} from "./company-general/company-general.component";
import {CompanyBenefitsComponent} from "./company-benefits/company-benefits.component";
import {CompanyMediaComponent} from "./company-media/company-media.component";
import {CompanyMediaEditComponent} from "./company-media/company-media.edit.component";
import {CompanyFaqComponent} from "app/company-admin/company-faq/company-faq.component";
import {CompanyFaqEditComponent} from "./company-faq/company-faq.edit.component";
import {NotificationComponent} from "./notification/notification.component";
import {NotificationQueryComponent} from "./notification/notification.query.component";
import {NotificationReviewComponent} from "./notification/notification.review.component";

/** The top level state(s) */
export let COMPANY_ADMIN_STATES: Ng2StateDeclaration[] = [
  {
    name: 'app.company-admin',
    url: '/company-admin',
    views: {
      content: {
        component: CompanyAdminHomeComponent
      },
      header: {
        component: CompanyAdminHeaderComponent
      }
    }
  },{
    name: 'app.company-admin.general',
    url: '/general',
    component:CompanyGeneralComponent
  },{
    name: 'app.company-admin.benefits',
    url: '/benefits',
    component:CompanyBenefitsComponent
  },{
    name: 'app.company-admin.multimedia',
    url: '/multimedia',
    component:CompanyMediaComponent
  },{
    name: 'app.company-admin.multimedia-edit',
    url: '/multimedia-edit/:contentId',
    component:CompanyMediaEditComponent
  },{
    name: 'app.company-admin.multimedia-create',
    url: '/multimedia-create',
    component:CompanyMediaEditComponent
  },{
    name: 'app.company-admin.faq',
    url: '/faq',
    component:CompanyFaqComponent
  },{
    name: 'app.company-admin.faq-edit',
    url: '/faq-edit/:faqId',
    component:CompanyFaqEditComponent
  },{
    name: 'app.company-admin.faq-create',
    url: '/faq-create',
    component:CompanyFaqEditComponent
  },{
    name: 'app.company-admin.notifications',
    url: '/notifications',
    component:NotificationComponent
  },{
    name: 'app.company-admin.notification-review',
    url: '/notifications/review/:feedbackId',
    component:NotificationReviewComponent
  },{
    name: 'app.company-admin.notification-query',
    url: '/notifications/query/:notificationId',
    component:NotificationQueryComponent
  }
];
