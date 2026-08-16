import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LocationStrategy, HashLocationStrategy, CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

import { IconModule, IconSetModule, IconSetService } from '@coreui/icons-angular';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};

import { AppComponent } from './app.component';

// Import containers
import { DefaultLayoutComponent } from './containers';


const APP_CONTAINERS = [
  DefaultLayoutComponent
];

import {
  AppAsideModule,
  AppBreadcrumbModule,
  AppHeaderModule,
  AppFooterModule,
  AppSidebarModule,
} from '@coreui/angular';

// Import routing module
import { AppRoutingModule } from './app.routing';

// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { ModalModule } from 'ngx-bootstrap/modal';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ProgressbarModule } from 'ngx-bootstrap/progressbar';
import { ChartsModule } from 'ng2-charts';
import { OrderComponent } from './views/order/order.component';
import { TokenInterceptorService } from './service/interceptor.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxSpinnerModule, NgxSpinnerService } from 'ngx-spinner';
import { DataTableModule } from '@pascalhonegger/ng-datatable';
import { ToastrModule } from 'ngx-toastr';
import { SharedModule } from './shared/shared.module';
import { FulfilmentConfirmComponent, SingleFulfilmentComponent } from './views/fulfilment/single-fulfilment.component';
import { LoginComponent } from './views/login/login.component';
import { AuthService } from './service/auth.service';
import { Authentication } from './service/auth.gaurd';
import { BulkFulfilmentComponent } from './views/fulfilment/bulkfulfilment.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),
    CommonModule,
    DataTableModule,
    AppRoutingModule,
    AppAsideModule,
    AppBreadcrumbModule.forRoot(),
    AppFooterModule,
    AppHeaderModule,
    AppSidebarModule,
    PerfectScrollbarModule,
    ProgressbarModule.forRoot(),
    BsDatepickerModule.forRoot(),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    TabsModule.forRoot(),
    ChartsModule,
    IconModule,
    IconSetModule.forRoot(),
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
    }),
    NgxSpinnerModule,
    SharedModule
  ],
  declarations: [
    AppComponent,
    ...APP_CONTAINERS,
    OrderComponent,
    LoginComponent,
    SingleFulfilmentComponent,
    FulfilmentConfirmComponent,
    BulkFulfilmentComponent
  ],
  providers: [
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    },
    AuthService,
    Authentication,
    NgxSpinnerService,
    {
      provide: HTTP_INTERCEPTORS, 
      useClass: TokenInterceptorService, 
      multi: true
    },
    IconSetService,
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
