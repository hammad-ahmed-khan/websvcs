import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { INavData } from '@coreui/angular';
import { AuthService } from '../../service/auth.service';
import { navItems } from '../../_nav';

@Component({
    selector: 'app-dashboard',
    templateUrl: './default-layout.component.html'
})
export class DefaultLayoutComponent implements OnInit {
    public sidebarMinimized = false;
    public navItems = [];

    constructor(private auth: AuthService,
                private router: Router) { }

    ngOnInit(): void {
        const authMenuItems = new Array<INavData>();
        navItems.forEach((menu) => this.authorizeMenu(menu, authMenuItems));
        this.navItems = authMenuItems;
        this.router.navigate([this.findFirstMenu(authMenuItems).url]);
    }

    private findFirstMenu = (authMenuItems: INavData[]): INavData => {
        let dMenu: INavData;
        authMenuItems.some(m => {
            if (m.children?.length) {
                dMenu = this.findFirstMenu(m.children);
            } else {
                dMenu = m;
                return true;
            }
        });
        return dMenu;
    }

    private authorizeMenu = (menu: INavData, navItems: INavData[]) => {
        const nMenu = {...menu};
        if (menu.attributes?.privilege) {
            if (this.auth.has(menu.attributes?.privilege)) {
                navItems.push(nMenu);
                return true;
            }
        } else if (menu.children?.length) {
            nMenu.children = new Array<INavData>();
            let hasChild = false;
            menu.children.forEach(cM => hasChild = this.authorizeMenu(cM, nMenu.children) || hasChild);
            if (hasChild) {
                navItems.push(nMenu);
            }
        } else {
            navItems.push(nMenu);
            return true;
        }
    }

    toggleMinimize(e) {
        this.sidebarMinimized = e;
    }

    logout = () => {
        this.auth.logout();
    }
}
