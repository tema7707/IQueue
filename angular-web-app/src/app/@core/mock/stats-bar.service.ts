import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StatsBarData } from '../data/stats-bar';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable()
export class StatsBarService extends StatsBarData {

  constructor(private http:HttpClient){ 
    super();
  }

  getStatsBarData(): Observable<number[]> {
    return this.http.get('//localhost:8080/test').pipe(
      map((data : number[])=>{
      return data;
      }));
  }
}
