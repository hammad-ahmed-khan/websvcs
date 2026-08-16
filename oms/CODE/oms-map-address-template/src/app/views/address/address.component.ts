import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommonModule, NgStyle } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, ButtonDirective, FormCheckComponent } from '@coreui/angular';
import { GoogleMap, MapAdvancedMarker, MapGeocoder } from '@angular/google-maps';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../../environments/environment';
import { ToastrService } from 'ngx-toastr';
import { NgxSpinnerService } from 'ngx-spinner';
import { delay, of } from 'rxjs';
import { OrderService } from './../../service/app.service';

@Component({
    selector: 'app-address-controls',
    templateUrl: './address.component.html',
    styleUrls: ['./address.component.scss'],
    standalone: true,
    imports: [FormCheckComponent, RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, ButtonDirective, NgStyle, CommonModule, GoogleMap, MapAdvancedMarker]
})
export class AddressComponent implements OnInit {

  position!: google.maps.LatLng | undefined | null;

  location!: google.maps.GeocoderGeometry;

  addressForm: FormGroup = new FormGroup({
    type:new FormControl(),
    orderNo:new FormControl(), 
    addressType: new FormControl(null, [Validators.required]),
    lat: new FormControl(null, [Validators.required]),
    long: new FormControl(),
    shortaddress: new FormControl(null, [Validators.required, Validators.pattern('^[A-Z]{4}[0-9]{4}$')])
  });

  options: google.maps.MapOptions = {
    zoomControl: true,
    disableDefaultUI: true,
    fullscreenControl: true,
    keyboardShortcuts: false,
    draggableCursor: 'icon',
    draggingCursor: 'icon',
    mapId: 'DEMO_MAP_ID'
  };

  searchBox!: google.maps.places.Autocomplete;

  @ViewChild('searchPlaceInput') searchPlaceInput!: ElementRef<HTMLInputElement>;

  @ViewChild('maps') maps!: GoogleMap;

  constructor(private cdRef:ChangeDetectorRef, activateRoute: ActivatedRoute,
        private http: HttpClient, private route: Router, private toastr: ToastrService,
        private spinner: NgxSpinnerService, private geocoder: MapGeocoder
  ) {
    const orderNo = activateRoute.snapshot.paramMap.get('orderNo');
    const type = activateRoute.snapshot.paramMap.get('type');
    if (!(type?.toLowerCase() == 'order' || type?.toLowerCase() == 'invoice')) {
      OrderService.orderStatus = 'INVALID_URL';
      this.route.navigate(['']);
      return;
    }
    google.maps.importLibrary('places');
    this.addressForm.patchValue({orderNo, type});
  }

  ngOnInit(): void {
    this.spinner.show();
    this.http.get(environment.baseAPIUrl + '/order/validate/' + this.addressForm.get('type')?.value + '/' + this.addressForm.get('orderNo')?.value, {
      responseType: 'text'
    }).subscribe({
      next: (status) => {
        this.spinner.hide();
        if (status != 'OK') {
          OrderService.orderStatus = status;
          this.route.navigate(['']);
        }
      }, error:(e) => {
        OrderService.orderStatus = 'INVALID';
        this.spinner.hide();
        this.route.navigate([''])
      }
    });
    this.geocoder.geocode({
      componentRestrictions: {country: "SA"},
      address: 'Saudi Arabia'
    }).subscribe(result => {
      this.location = result.results.at(0)?.geometry!;
      this.searchBox = new google.maps.places.Autocomplete(this.searchPlaceInput.nativeElement, {
        componentRestrictions: {country: "SA"}
      });
      this.searchBox.addListener("place_changed", () => {
        this.position = this.searchBox.getPlace().geometry?.location;
        this.cdRef.detectChanges();
        if (this.position) {
          this.maps.googleMap?.panTo(this.position);
          this.addressForm.get('lat')?.setValue(this.position.lat());
          this.addressForm.get('long')?.setValue(this.position.lng());
        }
      });
    });
  }

  switchAddrType = (latLongType: boolean) => {
    if (latLongType) {
      of(null).pipe(
        delay(10)
      ).subscribe(() => {
        this.maps.fitBounds(this.location?.bounds!);
      });
      this.addressForm.get('shortaddress')?.disable();
      this.addressForm.get('lat')?.enable();
    } else {
      this.addressForm.get('lat')?.disable();
      this.addressForm.get('shortaddress')?.enable();
    }
    this.addressForm.patchValue({
      shortaddress: null,
      lat: null,
      long: null
    });
    this.position = undefined;
  }

  selectLocation(event: google.maps.MapMouseEvent) {
    this.position = event.latLng;
    this.addressForm.get('lat')?.setValue(this.position?.lat());
    this.addressForm.get('long')?.setValue(this.position?.lng());
    this.searchPlaceInput.nativeElement.value = this.position?.toUrlValue()!;
  }

  validateAddress() {
    if (this.addressForm.valid) {
      this.spinner.show();
      this.http.post(environment.baseAPIUrl + '/order/address', this.addressForm.getRawValue(), {
        responseType: 'text'
      }).subscribe({
        next: (status) => {
          this.spinner.hide();
          if (status == 'OK') {
            this.addressForm.disable();
            this.toastr.success('Address verified successfully<br>Please close the window', 'Success', {
              enableHtml: true,
              progressBar: true
            }).onHidden.subscribe(() => window.self.close());
          } else {
            this.toastr.error(status, 'Error');
          }
        },
        error: () => {
          this.spinner.hide();
          this.toastr.error('Error while validating address', 'Error');
        }
    });
    }
  }

  closeWin = () => window.self.close();
}
