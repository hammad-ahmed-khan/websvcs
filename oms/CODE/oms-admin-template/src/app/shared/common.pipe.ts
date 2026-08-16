import { Pipe, PipeTransform } from "@angular/core";
import * as moment from 'moment';

@Pipe({
    name: 'mdate'
})
export class MomentDatePipe implements PipeTransform {

    transform(value: number, format?: string): string {
        return value ? moment(value).format(format || 'DD-MMM-YYYY hh:mm:A') : '';
    }
}

@Pipe({
    name: 'filter'
})
export class FilterPipe implements PipeTransform {

    transform(value: any[], param: any) {
        const keys = Object.keys(param);
        if (value?.length && keys?.length) {
            return value.filter((v) => keys.every((k) => !param[k] || param[k] == v[k] ));
        }
        return value;
    }
}
