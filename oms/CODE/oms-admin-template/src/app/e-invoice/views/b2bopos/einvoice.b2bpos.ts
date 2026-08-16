import { HttpClient } from "@angular/common/http";
import { Component } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import * as moment from "moment";
import { ToastrService } from "ngx-toastr";

@Component({
    templateUrl: "einvoice.b2bpos.html"
})
export class B2BPOSComponent {

    searchForm: FormGroup;

    invoices: any[];

    constructor(private http: HttpClient,
                private toast: ToastrService) { }

    ngOnInit(): void {
        this.searchForm = new FormGroup({
            invoiceNumber: new FormControl(''),
            fromDate: new FormControl(new Date(), [Validators.required]),
            toDate: new FormControl(new Date(), [Validators.required]),
            status: new FormControl('')
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
            
            this.http.get('b2bpos', { params }).subscribe((data: any[]) => {
                this.invoices = data;
                if (!data?.length) {
                    this.toast.error('No Orders Found', 'Error');
                } else if (data.length > 1000) {
                    this.toast.warning('Search criteria has more than 1000 records.', 'Info');
                }
            });
        }
    }
}