import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';

@Component({
  selector: 'ngx-ecommerce',
  templateUrl: './e-commerce.component.html',
})
export class ECommerceComponent implements OnInit {
  age : number = 0;

  constructor (private http: HttpClient) {
    this.Update();
  }

  ngOnInit(): void {
    this.Update();
  }

  Update() {
    this.http.get('//localhost:8080/stats/age?company='+AppModule.COMPANY)
    .pipe().subscribe(
      (data : number) => {
        this.age = data;
      }
    );
  }
}
