import { NbMenuItem } from '@nebular/theme';
import { AppModule } from '../app.module';

export const MENU_ITEMS: NbMenuItem[] = [
  { 
    title: AppModule.COMPANY.toString(),
    icon: 'nb-bar-chart',
    link: '/pages/dashboard',
    home: true,
  },
  {
    title: 'BRANCHES',
    group: true,
  }
];
