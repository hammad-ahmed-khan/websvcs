import { Routes } from '@angular/router';
import { DefaultLayoutComponent } from './layout';

export const routes: Routes = [
  {
    path: '',
    component: DefaultLayoutComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: ':type/:orderNo',
        loadComponent: () => import('./views/address/address.component').then(m => m.AddressComponent),
        data: {
          title: 'Address Verification'
        }
      },
      {
        path: '',
        loadComponent: () => import('./views/invalid-order/invalid-order.component').then(c => c.InvalidOrderComponent),
        data: {
          title: 'Address Verification'
        }
      },
      {
        path: '**', 
        redirectTo: ''
      }
    ]
  }
];
