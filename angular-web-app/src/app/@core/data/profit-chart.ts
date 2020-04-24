import { Observable } from 'rxjs/internal/Observable';

export interface ProfitChart {
  chartLabel: string[];
  data: Observable<number[][]>;
}

export abstract class ProfitChartData {
  abstract Update();
  abstract getProfitChartData(period: string): ProfitChart;
}
