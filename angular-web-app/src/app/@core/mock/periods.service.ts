import { Injectable } from '@angular/core';

@Injectable()
export class PeriodsService {
  getYears() {
    return [
      '2013', '2014', '2015', '2016',
      '2017', '2018', '2019',
    ];
  }

  getMonths() {
    return [
      'Jun',
      'Jul', 'Aug', 'Sep',
      'Oct', 'Nov', 'Dec',
      'Jan', 'Feb', 'Mar',
      'Apr', 'May'
    ];
  }

  getWeeks() {
    return [
      'Wed',
      'Thu',
      'Fri',
      'Sat',
      'Sun',
      'Mon',
      'Tue'
    ];
  }
}
