import { HttpClient } from "@angular/common/http";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { BsModalRef, BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { ToastrService } from "ngx-toastr";
import { Global } from "../../service/interceptor.service";

@Component({
    templateUrl: 'bulkfulfilment.component.html'
})
export class BulkFulfilmentComponent implements OnInit {

    fileControl: FormControl;
    file: File;
    fulfilments: any[];
    progress: any;
    showAction = false;

    @ViewChild('processModel') processModel:ModalDirective;

    constructor(private http: HttpClient,
                private toast: ToastrService) {

    }

    ngOnInit(): void {
        this.fileControl = new FormControl(null, [Validators.required]);
    }

    loadFulfilments = () => {
        if (this.fileControl.valid) {
            const formData = new FormData();
            formData.append('fulfilmentFile', this.file);
            this.http.post('bulkfulfilment', formData).subscribe((fulfilments: any[]) => {
                this.fulfilments = fulfilments;
                this.showAction = true;
            });
        }
    }

    onFileSelection = (event) => {
        const file:File = event.target.files[0];
        if (file && (file.type == 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel')) {
            this.file = file;
            this.fileControl.setErrors(null);
        } else if (!this.fileControl.hasError('required')) {
            this.fileControl.setErrors({invalidFile: true});
        }
    }

    cancelOrCreateFulfilment = (cancel: boolean) => {
        const fulfils = this.fulfilments?.filter((f) => f.valid);
        let totalFulfils = fulfils?.length;
        if (!totalFulfils) {
            this.toast.error('There is no valid fulfilemnt to process', 'Error');
            return;
        }
        this.showAction = false;
        this.http.put('bulkfulfilment', fulfils, {params: {cancel: cancel + ''}}).subscribe(() => {
            this.fulfilments = [];
            this.progress = {
                max: totalFulfils,
                status: [{value:0, type: 'success', label: '0 / ' + totalFulfils}, {value:0, type: 'danger', label: '0 / ' + totalFulfils}, {value:totalFulfils, type: 'primary'}],
                processed: 0
            }
            this.processModel.show();
            Global.LOADER_MANUAL = true;
            this.getFulfilemntStatus(totalFulfils);
        });
    }

    getFulfilemntStatus = (totalFulfils: number) => {
        if (totalFulfils > 0) {
            this.http.get('bulkfulfilment').subscribe(((fulfilment: any) => {
                this.progress.processed++;
                let v = null;
                if (fulfilment.valid) {
                    v = this.progress.status[0];
                } else {
                    v = this.progress.status[1];
                }
                v.value++;
                v.label = v.value + '/' + this.progress.max;
                this.progress.status[2].value--;
                this.fulfilments.push(fulfilment);
                this.getFulfilemntStatus(--totalFulfils);
            }));
        } else {
            Global.LOADER_MANUAL = false;
        }
    }

    downloadTemplate = () => {
        this.http.get('bulkfulfilment/template', { responseType: 'arraybuffer' }).subscribe((data) => {
            const blob = new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
            const a = document.createElement("a");
            a.href = URL.createObjectURL(blob);
            a.download = 'BulkFulfilment.xlsx';
            a.click();
        })
    }
}
