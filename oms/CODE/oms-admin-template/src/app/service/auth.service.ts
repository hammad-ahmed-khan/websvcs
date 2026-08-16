import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";

@Injectable()
export class AuthService {

    private user: User;

    constructor(private route: Router,
                private http: HttpClient) { }

    public isLoggedIn = (): boolean => this.user != null;

    public login = (token: string) => {
        sessionStorage.setItem('auth-token', token);
        this.http.get('user').subscribe((user: User) => {
            this.user = user;
            this.route.navigate(['home']);
        });
    }

    hasAll = (...privileges: string[]): boolean => privileges.every(this.has);

    hasAny = (privileges: string[]): boolean => privileges.some(this.has);

    has = (privilege: string): boolean => this.user.privileges?.includes(privilege);

    public getToken = ():string => sessionStorage.getItem('auth-token');

    logout = () => {
        sessionStorage.removeItem('auth-token');
        this.user = null;
        this.route.navigate(['login']);
    }
}

interface User {
    id: number;
    name?: string;
    roleId?: number;
    privileges?: string[];
}
