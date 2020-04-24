import { NgModule } from '@angular/core';

import { ThemeModule } from '../../@theme/theme.module';
import { Router } from '@angular/router';
import { AppModule } from '../../app.module';
import { BranchComponent } from './branch.component';

@NgModule({
  imports: [
    ThemeModule
  ],
  declarations: [
    BranchComponent,
  ],
})

export class BranchModule { 
  constructor (router : Router) {
    if (AppModule.LOGIN == null)
      router.navigateByUrl('http://localhost:4200/#/auth/login');
  }
}
