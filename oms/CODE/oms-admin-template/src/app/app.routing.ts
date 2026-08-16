import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

// Import Containers
import { DefaultLayoutComponent } from './containers';
import { Authentication } from './service/auth.gaurd';
import { BulkFulfilmentComponent } from './views/fulfilment/bulkfulfilment.component';
import { SingleFulfilmentComponent } from './views/fulfilment/single-fulfilment.component';
import { LoginComponent } from './views/login/login.component';

import { OrderComponent } from './views/order/order.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent,
    data: {
      title: 'Login Page'
    }
  },
  {
    path: 'home',
    component: DefaultLayoutComponent,
    data: {
      title: 'Home'
    },
    canActivate: [Authentication],
    canActivateChild: [Authentication],
    children: [
      {
        path: 'order',
        component: OrderComponent,
        data: {
          title: 'Orders',
          privilege: 'MENU_ORDERS'
        }
      },
      {
        path: 'fulfilment',
        component: SingleFulfilmentComponent,
        data: {
          title: 'Single Fulfilment',
          privilege: 'MENU_SINGLE_FULFIL'
        }
      },
      {
        path: 'bulkfulfilment',
        component: BulkFulfilmentComponent,
        data: {
          title: 'Bulk Fulfilment',
          privilege: 'MENU_BULK_FULFIL'
        }
      },
      {
        path: 'e-invoice',
        loadChildren: () => import('./e-invoice/einvoice.moudule').then(m => m.EInvoiceModule)
      }
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' }) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
