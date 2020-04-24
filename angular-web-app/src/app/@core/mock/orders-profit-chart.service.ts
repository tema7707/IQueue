import { of as observableOf,  Observable } from 'rxjs';
import { Injectable, OnInit } from '@angular/core';
import { OrdersChart, OrdersChartData } from '../data/orders-chart';
import { OrderProfitChartSummary, OrdersProfitChartData } from '../data/orders-profit-chart';
import { ProfitChart, ProfitChartData } from '../data/profit-chart';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';

@Injectable()
export class OrdersProfitChartService extends OrdersProfitChartData {

  private summary = [
    {
      title: 'All time',
      value: 0,
    },
    {
      title: 'Last Month',
      value: 0,
    },
    {
      title: 'Last Week',
      value: 0,
    },
    {
      title: 'Today',
      value: 0,
    },
  ];

  constructor(private ordersChartService: OrdersChartData,
              private profitChartService: ProfitChartData,
              private http:HttpClient) {
    super();
    this.Update();
  }

  public Update() {
    this.http.get('//localhost:8080/stats/all_orders?company='+AppModule.COMPANY).pipe().subscribe(
      (data : number) => {  
        this.summary[0].value = data;
      }
    );
    this.http.get('//localhost:8080/stats/last_month_orders?company='+AppModule.COMPANY).pipe().subscribe(
      (data : number) => {  
        this.summary[1].value = data;
      }
    );
    this.http.get('//localhost:8080/stats/last_week_orders?company='+AppModule.COMPANY).pipe().subscribe(
      (data : number) => {  
        this.summary[2].value = data;
      }
    );
    this.http.get('//localhost:8080/stats/today_orders?company='+AppModule.COMPANY).pipe().subscribe(
      (data : number) => {  
        this.summary[3].value = data;
      }
    );
    this.ordersChartService.Update();
    this.profitChartService.Update();
  }

  getOrderProfitChartSummary(): Observable<OrderProfitChartSummary[]> {
    return observableOf(this.summary);
  }

  getOrdersChartData(period: string): Observable<OrdersChart> {
    return observableOf(this.ordersChartService.getOrdersChartData(period));
  }

  getProfitChartData(period: string): Observable<ProfitChart> {
    return observableOf(this.profitChartService.getProfitChartData(period));
  }
}
