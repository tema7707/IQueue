import { Component } from '@angular/core';

import { MENU_ITEMS } from './pages-menu';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../app.module';

@Component({
  selector: 'ngx-pages',
  styleUrls: ['pages.component.scss'],
  template: `
    <ngx-sample-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-sample-layout>
  `,
})
export class PagesComponent {
  public static singleton : PagesComponent;
  menu = MENU_ITEMS;

  constructor(private http: HttpClient) {
    PagesComponent.singleton = this;
    this.menu = MENU_ITEMS;
    this.menu[0]['title'] = AppModule.COMPANY.toString();
    this.http.get('//localhost:8080/branches?company='+AppModule.COMPANY).pipe()
    .subscribe(
      (branch_names:JSON) => {
        let isLast = false;
        let i = 0;
        while (!isLast) {
          try {
            let address = branch_names[i]['address'];
            i++;
            this.menu.push({
              title: address,
              icon: 'nb-star',
              link: '/pages/branch/'+address
            });
          } catch {
            isLast = true;
            this.menu.push({
              title: 'Add New Branche',
              icon: 'nb-plus',
              link: '/pages/addbranch'
            });
          }
        }
      }
    );
  }
}
