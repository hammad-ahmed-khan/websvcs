import { HttpClient } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import * as moment from "moment";
import { ModalDirective } from "ngx-bootstrap/modal";
import { ToastrService } from "ngx-toastr";
import xmlFormat from 'xml-formatter';

@Component({
    templateUrl: 'einvoice.reporting.html',
    styleUrls: ['einvoice.reporting.scss']
})
export class ReportingComponent implements OnInit {

    searchForm: FormGroup;
    filterForm: FormGroup;

    invoices: any[];

    isXML = false;
    textControl : FormControl;

    @ViewChild('contentDisplayModel') contentDisplayModel:ModalDirective;

    constructor(private http: HttpClient,
        private toast: ToastrService) { }

    ngOnInit(): void {
        this.searchForm = new FormGroup({
            invoiceNumber: new FormControl(''),
            fromDate: new FormControl(new Date(), [Validators.required]),
            toDate: new FormControl(new Date(), [Validators.required]),
            source: new FormControl(''),
            invoiceType: new FormControl(''),
            status: new FormControl('')
        });

        this.textControl = new FormControl();

        this.filterForm = new FormGroup({
            source: new FormControl(''),
            invoiceType: new FormControl('')
        });
    }

    searchInvoices = () => {
        if (this.searchForm.valid) {
            const params = { ...this.searchForm.value };
            if (params.fromDate) {
                params.fromDate = moment(params.fromDate).format('DD-MM-YYYY');
            }
            if (params.toDate) {
                params.toDate = moment(params.toDate).format('DD-MM-YYYY');
            }
            
            this.http.get('einvoice', { params }).subscribe((data: any[]) => {
                this.invoices = data;
                if (!data?.length) {
                    this.toast.error('No Orders Found', 'Error');
                } else if (data.length > 1000) {
                    this.toast.warning('Search criteria has more than 1000 records.', 'Info');
                }
            });
        }
    }

    showContent = (isXML: boolean, invNo: string) => {
        this.isXML = isXML;
        this.http.get('einvoice' + (isXML ? '/xml/' : '/error/') + invNo, {
            responseType: 'text'
        }).subscribe((data: string) => {
            if (!isXML) {
                try {
                    data = JSON.stringify(JSON.parse(data), null, '\t');
                } catch(e) { }
            } else {
                data = xmlFormat(data, {
                    indentation: '\t', collapseContent: true, whiteSpaceAtEndOfSelfclosingTag: true
                });
            }
            this.textControl.setValue(data);
            this.contentDisplayModel.show();
        });
    }
}
