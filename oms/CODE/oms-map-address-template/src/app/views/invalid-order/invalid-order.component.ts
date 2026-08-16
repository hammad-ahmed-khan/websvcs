import { Component } from '@angular/core';
import { NgStyle, CommonModule } from '@angular/common';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, ButtonDirective, FormCheckComponent } from '@coreui/angular';
import { OrderService } from 'src/app/service/app.service';

@Component({
    selector: 'app-invalid-order-controls',
    templateUrl: './invalid-order.component.html',
    styleUrls: ['./invalid-order.component.scss'],
    standalone: true,
    imports: [FormCheckComponent, RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, ButtonDirective, NgStyle, CommonModule]
})
export class InvalidOrderComponent {

  orderStatus!: string;

  constructor() {
    this.orderStatus = OrderService.orderStatus;
  }
}
