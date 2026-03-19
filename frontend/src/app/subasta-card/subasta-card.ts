import {Component, input} from '@angular/core';
import {Subasta} from '../subasta.models';
import {RouterLink} from '@angular/router';
@Component({
  selector: 'app-subasta-card',
  imports: [RouterLink],
  template: `
    <section class="listing">
      <p class="listing-id">Subasta #{{ subasta().id }}</p>
      <h2 class="listing-heading">{{ subasta().nombre }}</h2>
      <a [routerLink]="['/details', subasta().id]">Ver precios</a>
    </section>
  `,
  styleUrls: ['./subasta-card.css'],
})
export class SubastaCard {
  subasta = input.required<Subasta>();
}
