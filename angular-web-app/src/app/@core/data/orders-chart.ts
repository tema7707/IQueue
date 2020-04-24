import { Observable } from 'rxjs/internal/Observable';

export interface OrdersChart { 
  chartLabel: string[];
  linesData:  Observable<number[][]>;
}

export abstract class OrdersChartData {
  abstract Update();
  abstract getOrdersChartData(period: string): OrdersChart;
}