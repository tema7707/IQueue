import { Component } from '@angular/core';

@Component({
  selector: 'ngx-earning-card',
  styleUrls: ['./earning-card.component.scss'],
  templateUrl: './earning-card.component.html',
})
export class EarningCardComponent {

  flipped = true;

  toggleFlipView() {
    this.flipped = !this.flipped;
  }
}
