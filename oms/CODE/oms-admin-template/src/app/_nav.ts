import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  {
    name: 'Orders',
    url: '/home/order',
    icon: 'icon-book-open',
    attributes: {
        privilege: 'MENU_ORDERS'
    }
  },
  {
    name: 'Fulfilment',
    url: '/fulfilment',
    icon: 'icon-basket',
    children: [
      {
        name: 'Single Fulfilment',
        url: '/home/fulfilment',
        icon: 'icon-bag',
        attributes: {
            privilege: 'MENU_SINGLE_FULFIL'
        }
      },
      {
        name: 'Bulk Fulfilment',
        url: '/home/bulkfulfilment',
        icon: 'icon-social-dropbox',
        attributes: {
            privilege: 'MENU_BULK_FULFIL'
        }
      }
    ]
  }, 
  {
    name: 'E-Invoicing',
    url: '/e-invoicing',
    icon: 'fa fa-folder-open',
    children: [
        {
            name: 'Reported Invoices',
            url: '/home/e-invoice/reporting',
            icon: 'icon-envelope-letter',
            attributes: {
                privilege: 'MENU_EINV_REPORTING'
            }
        },
        {
            name: 'B2B EBS Invoices',
            url: '/home/e-invoice/ebs',
            icon: 'fa fa-envelope',
            attributes: {
                privilege: 'MENU_EINV_B2BEBS'
            }
        },
        {
            name: 'B2B POS Invoices',
            url: '/home/e-invoice/b2bpos',
            icon: 'fa fa-envelope-o',
            attributes: {
                privilege: 'MENU_EINV_B2BPOS'
            }
        }
    ]
  }
];
