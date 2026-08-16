import { HttpClient } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, ValidationErrors, Validators } from "@angular/forms";
import { ToastrService } from "ngx-toastr";

@Component({
    templateUrl: './order.component.html'
})
export class OrderComponent implements OnInit {

    searchForm: FormGroup;

    basicSearch = true;
    showDetail = false;
    orders: any[];
    orderDetail: any;

    filterForm: FormGroup;

    constructor(private http: HttpClient,
            private toast: ToastrService) { }

    ngOnInit(): void {
        this.searchForm = new FormGroup({
            orderNumber: new FormControl(),
            file: new FormControl(),
            fromDate: new FormControl(new Date(), [Validators.required]),
            toDate: new FormControl(new Date(), [Validators.required]),
            country: new FormControl(''),
            orderType: new FormControl(''),
            deliveryType: new FormControl(''),
            paymentType: new FormControl(''),
            status: new FormControl('')
        }, {validators : (group: FormGroup): ValidationErrors | null => {
                let anyField = ['orderNumber', 'file'].find(key => !!group.get(key).value);
                if (this.basicSearch && !anyField ) {
                    return {
                        anyField : {
                            error: true
                        }
                    }
                }
                return null;
        }});
        this.toogleSearch(true);
        this.filterForm = new FormGroup({
            countryName: new FormControl(''),
            deliveryType: new FormControl(''),
            status: new FormControl('')
        });
    }

    toogleSearch = (basicSearch: boolean) => {
        if (basicSearch) {
            this.searchForm.get('orderNumber').enable();
            this.searchForm.get('file').enable();
            this.searchForm.get('fromDate').disable();
            this.searchForm.get('toDate').disable();
            this.searchForm.get('country').disable();
            this.searchForm.get('orderType').disable();
            this.searchForm.get('deliveryType').disable();
            this.searchForm.get('paymentType').disable();
            this.searchForm.get('status').disable();
        } else {
            this.searchForm.get('orderNumber').disable();
            this.searchForm.get('file').disable();
            this.searchForm.get('fromDate').enable();
            this.searchForm.get('toDate').enable();
            this.searchForm.get('country').enable();
            this.searchForm.get('orderType').enable();
            this.searchForm.get('deliveryType').enable();
            this.searchForm.get('paymentType').enable();
            this.searchForm.get('status').enable();
        }
        this.basicSearch = basicSearch;
    }

    searchOrder = () => {
        if (this.searchForm.valid) {
            const searchParam = {...this.searchForm.value};
            if (searchParam.fromDate) {
                searchParam.fromDate = searchParam.fromDate.getTime();
            }
            if (searchParam.toDate) {
                searchParam.toDate = searchParam.toDate.getTime();
            }
            Object.keys(searchParam).forEach((key) => {
                if (!searchParam[key]) {
                    delete searchParam[key];
                }
            });
            this.http.get('order', {params: searchParam}).subscribe((orders: any[]) => {
                this.orders = orders;
                if (!orders?.length) {
                    this.toast.error('No Orders Found', 'Error');
                } else if (orders.length > 1000) {
                    this.toast.warning('Search criteria has more than 1000 records.', 'Info');
                }
            });
        }
    }

    openOrderDetail = (order: any) => {
        this.http.get('order/detail/' + order.omsOrderNumber).subscribe((orderInfo: any) => {
            order = { ...order, ...orderInfo };
            this.orderDetail = order;
            this.showDetail = true;
        });
    }

    backToSearch = () => {
        this.showDetail = false;
        this.orderDetail = undefined;
    }

    showCancellationDetail = () => {
        if (!this.orderDetail.cancellationDetail?.length) {
            this.http.get('order/cancel/' + this.orderDetail.orderNumber).subscribe((cancellationDetail: any[]) => {
                this.orderDetail.cancellationDetail = cancellationDetail;
            });
        }
    }

    showAddressInfo = () => {
        if (!this.orderDetail.addressInfo) {
            this.http.get('order/address/' + this.orderDetail.omsOrderNumber).subscribe((addressInfo: any) => {
                this.orderDetail.addressInfo = addressInfo;
            });
        }
    }

    showBookingDetail = () => {
        if (!this.orderDetail.bookings) {
            this.http.get('order/booking/' + this.orderDetail.orderNumber).subscribe((bookings: any) => {
                if (!bookings?.length) {
                    bookings = [];
                }
                this.orderDetail.bookings = bookings;
            });
        }
    }

    showReturnDetail = () => {
        if (!this.orderDetail.returnDetail?.length) {
            this.http.get('order/return/' + this.orderDetail.orderNumber).subscribe((returnDetail: any[]) => {
                this.orderDetail.returnDetail = returnDetail;
            });
        }
    }
}
