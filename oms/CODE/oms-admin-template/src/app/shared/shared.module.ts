import { NgModule } from "@angular/core";
import { FilterPipe, MomentDatePipe } from "./common.pipe";

@NgModule({
    declarations: [ MomentDatePipe, FilterPipe ],
    exports: [ MomentDatePipe, FilterPipe ]
})
export class SharedModule { }
