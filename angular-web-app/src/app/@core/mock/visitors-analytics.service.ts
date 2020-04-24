import { Injectable } from '@angular/core';
import { of as observableOf, Observable } from 'rxjs';
import { PeriodsService } from './periods.service';
import { OutlineData, VisitorsAnalyticsData } from '../data/visitors-analytics';
import { HttpClient } from '@angular/common/http';
import { AppModule } from '../../app.module';

@Injectable()
export class VisitorsAnalyticsService extends VisitorsAnalyticsData {

  constructor(private periodService: PeriodsService, private http: HttpClient) {
    super();
  }

  async Update() {
    let data = await this.http.get('//localhost:8080/stats/all_orders_m?company='+AppModule.COMPANY)
    .pipe().toPromise();
    
    this.pieChartValue[1] = 0;
    for (let i = 0; i < 12; i++) {
      this.innerLinePoints[i] = data[2][i] + data[1][i];
      this.pieChartValue[1] += data[2][i] + data[1][i];
    }
       

    data = await this.http.get('//localhost:8080/stats/unique_orders_m?company='+AppModule.COMPANY).pipe()
    .toPromise();
    this.pieChartValue[0] = 0;
        for (let i = 0; i < 12; i++) {
          this.outerLinePoints[i] = data[i];
          this.pieChartValue[0] += data[i];
        }
  }

  private pieChartValue : number[] = [0, 10];
  private innerLinePoints: number[] = [0,0,0,0,0,0,0,0,0,0,0,0,0];
  private outerLinePoints: number[] = [0,0,0,0,0,0,0,0,0,0,0,0,0];

  private generateOutlineLineData(): OutlineData[] {
    const months = this.periodService.getMonths();

    return this.outerLinePoints.map((p, index) => {
      const monthIndex = index;
      const label = months[monthIndex];

      return {
        label,
        value: p,
      };
    });
  }

  getInnerLineChartData(): Observable<number[]> {
    return observableOf(this.innerLinePoints);
  }

  getOutlineLineChartData(): Observable<OutlineData[]> {
    return observableOf(this.generateOutlineLineData());
  }

  getPieChartData(): Observable<number[]> {
    return observableOf(this.pieChartValue);
  }
}
