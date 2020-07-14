import {Ng2StateDeclaration} from "@uirouter/angular";
import {AdminHeaderComponent} from "./header/admin-header.component";
import {AdminHomeComponent} from "./home/admin.home.component";
import {CompanyEditComponent} from "app/admin/company-edit/company-edit.component";

export let ADMIN_STATES: Ng2StateDeclaration[] = [
  {
    name: 'app.admin-home',
    url: '/admin',
    views: {
      content: {
        component: AdminHomeComponent
      },
      header: {
        component: AdminHeaderComponent
      }
    }
  },{
    name: 'app.admin-company-edit',
    url: '/company-edit/:companyId',
    views: {
      content: {
        component: CompanyEditComponent
      },
      header: {
        component: AdminHeaderComponent
      }
    }
  },{
    name: 'app.admin-company-add',
    url: '/company-add',
    views: {
      content: {
        component: CompanyEditComponent
      },
      header: {
        component: AdminHeaderComponent
      }
    }
  }
];
