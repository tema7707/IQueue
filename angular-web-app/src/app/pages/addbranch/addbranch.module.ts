import { NgModule } from '@angular/core';

import { ThemeModule } from '../../@theme/theme.module';
import { Router } from '@angular/router';
import { AppModule } from '../../app.module';
import { AddBranchComponent } from './addbranch.component';
import { NbAlertModule } from '@nebular/theme/components/alert/alert.module';

@NgModule({
  imports: [
    ThemeModule,
    NbAlertModule
  ],
  declarations: [
    AddBranchComponent,
  ],
})

export class AddBranchModule { 
  constructor (router : Router) {
    if (AppModule.LOGIN == null)
      router.navigateByUrl('http://localhost:4200/#/auth/login');
  }
}
