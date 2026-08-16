import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable()
export class Authentication implements CanActivate, CanActivateChild {

    constructor(private auth: AuthService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        const hasSession =  this.auth.isLoggedIn();
        if (!hasSession) {
            this.router.navigate(['login']);
        }
        return hasSession;
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
        let authorized = false;
        authorized = !childRoute.data?.privilege || this.auth.hasAll(childRoute.data?.privilege);
        return authorized;
    }
}
