import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {SubastaCard} from '../subasta-card/subasta-card';
import {Subasta} from '../subasta.models';
import {SubastasService} from '../subastas.service';
@Component({
  selector: 'app-subastas',
  imports: [SubastaCard],
  template: `
    <section>
      <form>
        <input type="text" placeholder="Filtrar por subasta" #filter />
        <button class="primary" type="button" (click)="filterResults(filter.value)">Buscar</button>
      </form>
    </section>
    <section class="results">
      @for (subasta of filteredSubastas; track subasta.id) {
        <app-subasta-card [subasta]="subasta" />
      }
    </section>
  `,
  styleUrls: ['./subastas.css'],
})
export class Subastas {
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly subastasService = inject(SubastasService);
  subastas: Subasta[] = [];
  filteredSubastas: Subasta[] = [];

  constructor() {
    this.subastasService.getSubastas().then((subastas: Subasta[]) => {
      this.subastas = subastas;
      this.filteredSubastas = subastas;
      this.changeDetectorRef.markForCheck();
    }).catch(() => {
      this.subastas = [];
      this.filteredSubastas = [];
      this.changeDetectorRef.markForCheck();
    });
  }

  filterResults(text: string) {
    if (!text) {
      this.filteredSubastas = this.subastas;
      this.changeDetectorRef.markForCheck();
      return;
    }

    const term = text.toLowerCase().trim();
    this.filteredSubastas = this.subastas.filter((subasta) =>
      subasta.nombre.toLowerCase().includes(term),
    );
    this.changeDetectorRef.markForCheck();
  }
}