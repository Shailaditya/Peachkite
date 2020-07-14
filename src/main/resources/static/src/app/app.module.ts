import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import {UIRouterModule,UIView} from "@uirouter/angular";
import { APP_STATES } from './app.states';
import { UserModule } from './user/user.module';
import { CompanyAdminModule } from './company-admin/company-admin.module';
import { ServiceModule } from './service/service.module';
import { BootstrapModalModule } from 'ng2-bootstrap-modal';
import {AuthInterceptor} from "./auth.interceptor";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {HttpClientModule} from '@angular/common/http';
import {DpDatePickerModule} from 'ng2-date-picker';
import {AdminModule} from "./admin/admin.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    HttpClientModule,
    UserModule,
    AdminModule,
    CompanyAdminModule,
    ServiceModule,
    BootstrapModalModule,
    DpDatePickerModule,
    UIRouterModule.forRoot({ states:APP_STATES, useHash: true ,otherwise:{state:'app.user'}})
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true,
  }],
  entryComponents: [AppComponent],
  bootstrap: [UIView]
})
export class AppModule { }
