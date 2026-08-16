import { HttpClient } from "@angular/common/http";
import { Component, EventEmitter, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from "ngx-toastr";
import { Global } from "../../service/interceptor.service";

@Component({
    templateUrl: 'single-fulfilment.component.html'
})
export class SingleFulfilmentComponent implements OnInit {

    searchForm: FormGroup;
    createFulfilment = false;
    fulfilments: any[];
    fulfilment: any;
    createForm: FormGroup;

    stores: any;
    wareHouses: any;
    suppliers: any;
    virtuals: any;
    sourceLocs: any;
    fulfimentLocs: any;

    constructor(private http: HttpClient,
            private toast: ToastrService,
            private modalService: BsModalService,
            private spinner: NgxSpinnerService) {
    }

    ngOnInit(): void {
        this.searchForm = new FormGroup({
            orderNumber: new FormControl(null, [Validators.required]),
            item: new FormControl('')
        });
        this.createForm = new FormGroup({
            srcLocationType: new FormControl('', [Validators.required]),
            srcLocation: new FormControl(null, [Validators.required]),
            fulfilLocationType: new FormControl('', [Validators.required]),
            fulfilLocation: new FormControl(null, [Validators.required]),
            requestQty: new FormControl(null, [Validators.required])
        });
    }

    searchFulfilment = () => {
        if (this.searchForm.valid) {
            this.http.get('fulfilment', {params: this.searchForm.value}).subscribe((fulfilments: any[]) => {
                this.fulfilments = fulfilments;
                if (!fulfilments?.length) {
                    this.toast.error('No Fulfilments Found', 'Error');
                }
            });
        }
    }

    openCancelFulfilmentForm = (fulfilment: any) => {
        const cancelQty = fulfilment.requestQty - fulfilment.cancelledQty;
        this.modalService.show(FulfilmentConfirmComponent, {
            backdrop: 'static',
            keyboard: false,
            class: 'modal-primary',
            initialState: {
                cancelQty
            }
        }).content.submitEmit.subscribe((cancelledQty) => {
            const body = {...fulfilment, exFulfilment: {...fulfilment}};
            body.cancelledQty = body.exFulfilment.cancelledQty = cancelledQty;
            this.http.put('fulfilment', body).subscribe((upFulfilment: any) => {
                fulfilment.cancelledQty = upFulfilment.cancelledQty;
                this.toast.success('Fulfilment cancelled successfully', 'Success');
            });
        });
    }

    loadCreateForm = (fulfilment: any, cancelAndCreate = false) => {
        fulfilment.cancelAndCreate = cancelAndCreate;
        if (this.stores) {
            this.showCreateForm(fulfilment);
        } else {
            Global.LOADER_MANUAL = true;
            this.spinner.show();
            Promise.all([this.http.get('util/store').toPromise(),
                        this.http.get('util/wh').toPromise(),
                        this.http.get('util/virtual').toPromise()]).then((results: any[]) => {
                this.spinner.hide();
                Global.LOADER_MANUAL = false;
                this.stores = results[0];
                this.wareHouses = results[1];
                this.virtuals = results[2];
                this.showCreateForm(fulfilment);
            })
        }
    }

    showCreateForm = (fulfilment: any) => {
        if (fulfilment.srcLocationType == 'ST') {
            this.sourceLocs = this.stores;
        } else if (fulfilment.srcLocationType == 'WH') {
            this.sourceLocs = this.wareHouses;
        } else if (fulfilment.srcLocationType == 'SU') {
            this.sourceLocs = this.suppliers;
        }
        if (fulfilment.fulfilLocationType == 'S') {
            this.fulfimentLocs = this.stores;
        } else if (fulfilment.fulfilLocationType == 'V') {
            this.fulfimentLocs = this.virtuals;
        }
        this.fulfilment = fulfilment;
        this.createFulfilment = true;
        const reqQty = (fulfilment.requestQty - (((fulfilment.itemCancelledQty || 0) - (fulfilment.itemOrderedQty - fulfilment.requestQty)) + (fulfilment.deliveryQty || 0)));
        this.createForm.patchValue({
            srcLocationType: fulfilment.srcLocationType,
            srcLocation: fulfilment.srcLocationType == 'WH' ? this.sourceLocs.find(s => s.physicalWH == fulfilment.srcLocation) : fulfilment.srcLocation,
            requestQty: reqQty,
            fulfilLocationType: fulfilment.fulfilLocationType,
            fulfilLocation: fulfilment.fulfilLocation
        });
        this.createForm.get('requestQty').setValidators([Validators.required, Validators.min(1), Validators.max(reqQty)]);
        this.createForm.get('requestQty').updateValueAndValidity();
        this.loadStockQty();
    }

    loadSRCLocation = () => {
        const srcLocType = this.createForm.get('srcLocationType').value;
        if (srcLocType == 'ST') {
            this.sourceLocs = this.stores;
        } else if (srcLocType == 'WH') {
            this.sourceLocs = this.wareHouses;
        } else if (srcLocType == 'SU') {
            this.sourceLocs = this.suppliers;
        }
        this.createForm.get('srcLocation').setValue(null);
    }

    loadFulfilLocation = () => {
        const fulfilLocType = this.createForm.get('fulfilLocationType').value;
        if (fulfilLocType == 'S') {
            this.fulfimentLocs = this.stores;
        } else if (fulfilLocType == 'V') {
            this.fulfimentLocs = this.virtuals;
        } else if (fulfilLocType == 'W') {
            this.fulfimentLocs = this.wareHouses;
        }
        this.createForm.get('fulfilLocation').setValue(null);
    }

    cancelCreateFulfil = () => {
        this.createFulfilment = false;
        this.fulfilment = undefined;
    }

    createNewFulfilment = () => {
        if (this.createForm.valid) {
            const body = {...this.fulfilment, ...this.createForm.value, exFulfilment: {...this.fulfilment}};
            body.cancelledQty = body.exFulfilment.cancelledQty = body.requestQty - (body.cancelledQty ? ((body.cancelledQty || 0) - (body.itemCancelledQty || 0)) : 0);
            if (body.srcLocationType == 'WH') {
                body.srcLocCHId = body.srcLocation.wh
                body.srcLocation = body.srcLocation.physicalWH;
            }
            if (body.fulfilLocationType == 'W') {
                body.fulfilLocCHId = body.fulfilLocation.wh
                body.fulfilLocation = body.fulfilLocation.physicalWH;
            }
            this.http.post('fulfilment', body, {
                params: {cancelAndCreate: this.fulfilment.cancelAndCreate}
            }).subscribe((newFulfilment: any) => {
                if (this.fulfilment.cancelAndCreate) {
                    this.fulfilment.cancelledQty += body.cancelledQty;
                }
                delete this.fulfilment['cancelAndCreate'];
                this.fulfilments.push(newFulfilment);
                this.toast.success('Fulfilment created successfully', 'Success');
                this.createFulfilment = false;
                this.fulfilment = undefined;
            });
        }
    }

    loadStockQty = () => {
        const params = {...this.createForm.getRawValue()};
        if (params.srcLocation && params.srcLocationType && params.srcLocationType != 'SU') {
            params.item = this.fulfilment.item;
            if (params.srcLocationType == 'WH') {
                params.srcLocCHId = params.srcLocation.wh
                params.srcLocation = params.srcLocation.physicalWH;
            }
            if (params.fulfilLocationType == 'W') {
                params.fulfilLocCHId = params.fulfilLocation.wh
                params.fulfilLocation = params.fulfilLocation.physicalWH;
            }
            this.http.get('fulfilment/stock', {params}).subscribe((availQty: number) => {
                this.fulfilment.availableQty = availQty;
                this.validateStock(params.requestQty);
            });
        }
    }

    validateStock = (reqQty: number) => {
        const cntrl = this.createForm.get('srcLocation');
        const srcLocType = this.createForm.get('srcLocationType').value;
        if (srcLocType != 'SU' && this.fulfilment.availableQty < reqQty) {
            cntrl.setErrors({notAvailable: true});
        } else if (cntrl.hasError('notAvailable')) {
            delete cntrl.errors['notAvailable'];
        }
    }
}

@Component({
    template: `
                <div class="modal-header">
                    <h4 class="pull-left">Cancel Fulfilment</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-9"><label>Enter the fulfilment cancel quantity </label></div>
                        <div class="col-3">
                            <input type="number" [formControl]="cancelQtyCtrl" [ngClass]="{'is-invalid': submitted && !cancelQtyCtrl.valid}" class="form-control" />
                            <div *ngIf="submitted && cancelQtyCtrl.hasError('required')" class="invalid-feedback">
                                This field is required
                            </div>
                            <div *ngIf="submitted && cancelQtyCtrl.hasError('min')" class="invalid-feedback">
                                Please enter minimum 1 Qty.
                            </div>
                            <div *ngIf="submitted && cancelQtyCtrl.hasError('max')" class="invalid-feedback">
                                Please enter maximum {{cancelQty}} Qty.
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" (click)="cancelFulfilemnt()">Confirm</button>
                    <button type="button" class="btn btn-danger" (click)="bsModalRef.hide();">Cancel</button>
                </div>
            `
})
export class FulfilmentConfirmComponent implements OnInit {

    cancelQtyCtrl: FormControl = new FormControl();

    submitted = false;
    cancelQty: number;

    public submitEmit: EventEmitter<number>;

    constructor(public bsModalRef: BsModalRef) {
        this.submitEmit = new EventEmitter();
    }

    ngOnInit(): void {
        this.cancelQtyCtrl.setValue(this.cancelQty);
        this.cancelQtyCtrl.setValidators([Validators.required, Validators.min(1), Validators.max(this.cancelQty)]);
        this.cancelQtyCtrl.updateValueAndValidity();
    }

    cancelFulfilemnt = () => {
        this.submitted = true;
        if (this.cancelQtyCtrl.valid) {
            this.submitEmit.emit(this.cancelQtyCtrl.value );
            this.bsModalRef.hide();
        }
    }
}
