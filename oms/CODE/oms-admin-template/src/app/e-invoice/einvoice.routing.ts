import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { B2BPOSComponent } from "./views/b2bopos/einvoice.b2bpos";
import { EBSComponent } from "./views/ebs/einvoice.ebs";
import { ReportingComponent } from "./views/reporting/einvoice.reporting";

@NgModule({
    imports: [ RouterModule.forChild([
        {
            path: '',
            redirectTo: 'reporting'
        },
        {
            data: {
                title: 'Reported Invoices'
            },
            path: 'reporting',
            component: ReportingComponent
        },
        {
            data: {
                title: 'EBS Invoices'
            },
            path: 'ebs',
            component: EBSComponent
        },
        {
            data: {
                title: 'B2B POS Invoices'
            },
            path: 'b2bpos',
            component: B2BPOSComponent
        }
    ]) ],
    exports: [ RouterModule ]
})
export class EInvoiceRoutingodule { }
