import { Injectable } from '@angular/core';
import { of as observableOf, Observable } from 'rxjs';
import { LiveUpdateChart, PieChart, EarningData } from '../data/earning';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';

@Injectable()
export class EarningService extends EarningData {

  private currentDate: Date = new Date();
  private currentValue = Math.random() * 1000;
  private ONE_DAY = 24 * 3600 * 1000;
  private pieChartData = [
    {
      value: 50,
      name: 'Male',
    },
    {
      value: 50,
      name: 'Female',
    }
  ];;

  constructor (private http:HttpClient) {
    super();
  }

  private liveUpdateChartData = {
    bitcoin: {
      liveChart: [],
      delta: {
        up: true,
        value: 4,
      },
      dailyIncome: 45895,
    },
    tether: {
      liveChart: [],
      delta: {
        up: false,
        value: 9,
      },
      dailyIncome: 5862,
    },
    ethereum: {
      liveChart: [],
      delta: {
        up: false,
        value: 21,
      },
      dailyIncome: 584,
    },
  };

  getDefaultLiveChartData(elementsNumber: number) {
    this.currentDate = new Date();
    this.currentValue = Math.random() * 1000;

    return Array.from(Array(elementsNumber))
      .map(item => this.generateRandomLiveChartData());
  }

  generateRandomLiveChartData() {
    this.currentDate = new Date(+this.currentDate + this.ONE_DAY);
    // this.currentValue = this.currentValue + Math.random() * 20 - 11;
    this.currentValue = this.currentValue + 1;

    if (this.currentValue < 0) {
      this.currentValue = Math.random() * 100;
    }

    return {
      value: [
        [
          this.currentDate.getFullYear(),
          this.currentDate.getMonth(),
          this.currentDate.getDate(),
        ].join('/'),
        Math.round(this.currentValue),
      ],
    };
  }

  getEarningLiveUpdateCardData(currency): Observable<any[]> {
    const data = this.liveUpdateChartData[currency.toLowerCase()];
    const newValue = this.generateRandomLiveChartData();

    data.liveChart.shift();
    data.liveChart.push(newValue);

    return observableOf(data.liveChart);
  }

  getEarningCardData(currency: string): Observable<LiveUpdateChart> {
    const data = this.liveUpdateChartData[currency.toLowerCase()];

    data.liveChart = this.getDefaultLiveChartData(150);

    return observableOf(data);
  }

  getEarningPieChartData(): Observable<PieChart[]> {
    this.http.get('//localhost:8080/stats/sex-piechart?company='+AppModule.COMPANY).pipe().subscribe(
    (data) => {  
      this.pieChartData[0].value = data[0];
      this.pieChartData[1].value = data[1];
    }
    );
    return observableOf(this.pieChartData);
  }
}
