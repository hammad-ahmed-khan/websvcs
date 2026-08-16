import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { DataTableModule } from "@pascalhonegger/ng-datatable";
import { BsDatepickerModule } from "ngx-bootstrap/datepicker";
import { BsDropdownModule } from "ngx-bootstrap/dropdown";
import { ModalModule } from "ngx-bootstrap/modal";
import { TooltipModule } from "ngx-bootstrap/tooltip";
import { SharedModule } from "../shared/shared.module";
import { EInvoiceRoutingodule } from "./einvoice.routing";
import { B2BPOSComponent } from "./views/b2bopos/einvoice.b2bpos";
import { EBSComponent } from "./views/ebs/einvoice.ebs";
import { ReportingComponent } from "./views/reporting/einvoice.reporting";

@NgModule({
    imports: [
        EInvoiceRoutingodule,
        BsDatepickerModule,
        BsDropdownModule,
        ModalModule.forChild(),
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        DataTableModule,
        SharedModule,
        TooltipModule
    ],
    declarations: [
        ReportingComponent,
        EBSComponent,
        B2BPOSComponent
    ]
})
export class EInvoiceModule { }