import {Ng2StateDeclaration} from '@uirouter/angular';
import {UserHomeComponent} from './userhome/userhome.component';
import {HeaderComponent} from './header/header.component';
import {ReviewComponent} from './review/review.component';
import {CompanyDetailComponent} from "./company-detail/company-detail.component";
/** The top level state(s) */
export let USER_STATES: Ng2StateDeclaration[] = [
    {
        name: 'app.user',
        url: '/user',
        views: {
            content: {
                component: UserHomeComponent
            },
            header: {
                component: HeaderComponent
            }
        }
     },
    {
      name: 'app.reset-password',
      url: '/reset-password/:token',
      views: {
        content: {
          component: UserHomeComponent
        },
        header: {
          component: HeaderComponent
        }
      }
    },
     {
         name: 'app.review',
         url: '/review/:companyId',
         views: {
             content: {
                 component: ReviewComponent
             },
             header: {
                 component: HeaderComponent
             }
         }
      },
      {
        name: 'app.company-detail',
        url: '/company-detail/:companyId',
        views: {
          content: {
            component: CompanyDetailComponent
          },
          header: {
            component: HeaderComponent
          }
        }
      }
];
