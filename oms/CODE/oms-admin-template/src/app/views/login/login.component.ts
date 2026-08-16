import { HttpClient, HttpResponse } from "@angular/common/http";
import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../service/auth.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html'
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;

    constructor(private http: HttpClient,
                private auth: AuthService) { }

    ngOnInit(): void {
        this.loginForm = new FormGroup({
            userName: new FormControl(null, [Validators.required]),
            password: new FormControl(null, [Validators.required])
        });
    }

    validateUser = () => {
        if (this.loginForm.valid) {
            this.http.post('login', this.loginForm.value, {
                observe: 'response'
            }).subscribe(resp => {
                this.auth.login(resp.headers.get('x-auth-token'));
            });
        }
    }
}