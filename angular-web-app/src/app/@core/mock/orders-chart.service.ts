import { Injectable } from '@angular/core';
import { PeriodsService } from './periods.service';
import { OrdersChart, OrdersChartData } from '../data/orders-chart';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { AppModule } from '../../app.module';

@Injectable()
export class OrdersChartService extends OrdersChartData {

  private year = [
    '2013',
    '2014',
    '2015',
    '2016',
    '2017',
    '2018',
    '2019',
  ];

  private data = { };

  constructor(private period: PeriodsService, private http:HttpClient) {
    super();
  }

  Update() {
    this.data = {
      week: this.getDataForWeekPeriod(),
      month: this.getDataForMonthPeriod(),
      year: this.getDataForYearPeriod(),
    };
  }

  private getDataForWeekPeriod(): OrdersChart {
    return {
      chartLabel: this.getDataLabels(7, this.period.getWeeks()),
      linesData: this.http.get('//localhost:8080/stats/all_orders_w?company='+AppModule.COMPANY).pipe(
          map((data : number[][])=>{
          return data;
          }))
    };
  }

  private getDataForMonthPeriod(): OrdersChart {
    return {
      chartLabel: this.getDataLabels(12, this.period.getMonths()),
      linesData: this.http.get('//localhost:8080/stats/all_orders_m?company='+AppModule.COMPANY).pipe(
        map((data : number[][])=>{
        return data;
        }))
    };
  }

  private getDataForYearPeriod(): OrdersChart {
    return {
      chartLabel: this.getDataLabels(7, this.year),
      linesData: this.http.get('//localhost:8080/stats/all_orders_y?company='+AppModule.COMPANY).pipe(
        map((data : number[][])=>{
        return data;
        }))
    };
  }

  getDataLabels(nPoints: number, labelsArray: string[]): string[] {
    const labelsArrayLength = labelsArray.length;
    const step = Math.round(nPoints / labelsArrayLength);

    return Array.from(Array(nPoints)).map((item, index) => {
      const dataIndex = Math.round(index / step);

      return index % step === 0 ? labelsArray[dataIndex] : '';
    });
  }

  getOrdersChartData(period: string): OrdersChart {
    return this.data[period];
  }
}
