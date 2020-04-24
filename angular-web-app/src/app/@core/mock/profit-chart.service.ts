import { Injectable } from '@angular/core';
import { PeriodsService } from './periods.service';
import { ProfitChart, ProfitChartData } from '../data/profit-chart';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';
import { map } from 'rxjs/operators';

@Injectable()
export class ProfitChartService extends ProfitChartData {

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

  constructor(private period: PeriodsService, private http: HttpClient) {
    super();
  }

  Update() {
    this.data = {
      week: this.getDataForWeekPeriod(),
      month: this.getDataForMonthPeriod(),
      year: this.getDataForYearPeriod(),
    };
  }

  private getDataForWeekPeriod(): ProfitChart {
    return {
      chartLabel: this.period.getWeeks(),
      data:
        this.http.get('//localhost:8080/stats/all_orders_w?company='+AppModule.COMPANY).pipe(
          map((data : number[][])=>{
            return [data[2], data[1], data[0]];
          })), //Hmmm
    };
  } 

  private getDataForMonthPeriod(): ProfitChart {
   
    return {
      chartLabel: this.period.getMonths(),
      data: this.http.get('//localhost:8080/stats/all_orders_m?company='+AppModule.COMPANY).pipe(
        map((data : number[][])=>{
          return [data[2], data[1], data[0]];
        })),
    };
  }

  private getDataForYearPeriod(): ProfitChart {
   
    return {
      chartLabel: this.year,
      data: this.http.get('//localhost:8080/stats/all_orders_y?company='+AppModule.COMPANY).pipe(
        map((data : number[][])=>{
          return [data[2], data[1], data[0]];
        })),
    };
  }

  getProfitChartData(period: string): ProfitChart {
    return this.data[period];
  }
}
