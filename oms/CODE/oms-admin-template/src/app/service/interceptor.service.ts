import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { NgxSpinnerService } from "ngx-spinner";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment";
import { catchError, map } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { AuthService } from "./auth.service";

@Injectable()
export class TokenInterceptorService implements HttpInterceptor {
    
    constructor(private spinner: NgxSpinnerService,
                private toast: ToastrService,
                private auth: AuthService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!Global.LOADER_MANUAL) {
            this.spinner.show();
        }
        let duplicate = null;
        const token = this.auth.getToken();
        if (token) {
            duplicate = req.clone({ 
                url: environment.baseURL + req.url,
                headers: req.headers.set('X-AUTH-TOKEN', token)
            });
        } else {
            duplicate = req.clone({ url: environment.baseURL + req.url });
        }
        return next.handle(duplicate)
            .pipe(catchError((error) => {
                this.spinner.hide();
                this.toast.error(ERROR_CODES[error.error] || error.error || ERROR_CODES['TECHINAL_ERROR'], 'Error');
                return error;
            }))
            .pipe(map<HttpEvent<any>, any>((evt: HttpEvent<any>) => {
                if (!Global.LOADER_MANUAL && evt instanceof HttpResponse) {
                    this.spinner.hide();
                }
                return evt;
            }));
    }
 }

enum ERROR_CODES {

    TECHINAL_ERROR = "Due to technical issue, not able to complete, please try again.",
    INVALID_QUANTITY_ON_CANCEL = "Invalid number of cancel quantity. please check.",
    BASE_FULFILMENT_EXCEPTION = "Error while calling base web service.",
    STOCK_NOT_AVAILABLE = "Stock not available in the location"
}

export class Global {
    public static LOADER_MANUAL = false;
}
