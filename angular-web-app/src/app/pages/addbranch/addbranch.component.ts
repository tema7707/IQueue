import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';
import { PagesComponent } from '../pages.component';

@Component({
  selector: 'ngx-addbranch',
  templateUrl: './addbranch.component.html',
})
export class AddBranchComponent {
  _error=false;
  _success=false;

  constructor(private http: HttpClient) { }

  add(form: NgForm) {
    let name = form.value.branch;
    let longitude = (''+form.value.longitude).replace(',','.');
    let latitude = (''+form.value.latitude).replace(',','.');
    this.http.get('//localhost:8080/add-branch?company='+AppModule.COMPANY
    +'&name='+name+'&longitude='+longitude+'&latitude='+latitude).pipe().subscribe(
      (data: boolean) => {
        this._success = data;
        this._error = !data;
        if (data) {
          PagesComponent.singleton.menu.pop();
          PagesComponent.singleton.menu.push(
            {
              title: name,
              icon: 'nb-star',
              link: '/pages/branch/'+name
            },
            {
              title: 'Add New Branche',
              icon: 'nb-plus',
              link: '/pages/addbranch'
            }
          );}
      }
    );
  }
}
