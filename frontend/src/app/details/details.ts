import {ChangeDetectorRef, Component, inject} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {SubastasService} from '../subastas.service';
import {Familia, PrecioSubasta, Producto, Subasta} from '../subasta.models';

interface ProductoFila {
  nombre: string;
  url: string | null;
  cortes: Partial<Record<number, number>>;
}

interface FamiliaTabla {
  familia: string;
  productos: ProductoFila[];
}
@Component({
  selector: 'app-details',
  imports: [],
  template: `
    <article>
      <section class="listing-description">
        <h1 class="listing-heading">{{ subasta?.nombre }}</h1>
        <p class="listing-location">Subasta #{{ subasta?.id }}</p>
      </section>

      <section class="listing-features">
        <h2 class="section-heading">Precios de corte por fecha</h2>

        @if (fechasDisponibles.length) {
          <div class="date-navigation">
            <button class="primary" type="button" (click)="changeDate(-1)" [disabled]="!canGoPrev()">
              Fecha anterior
            </button>
            <span>{{ fechaSeleccionada }}</span>
            <button class="primary" type="button" (click)="changeDate(1)" [disabled]="!canGoNext()">
              Fecha siguiente
            </button>
          </div>

          <div class="table-wrapper">
            <table class="auction-board">
              <tbody>
                @for (bloque of tablaFamilias; track $index) {
                  <tr class="familia-row">
                    <td class="familia-cell">{{ bloque.familia }}</td>
                    @for (corte of cortesColumnas; track corte) {
                      <td class="num-cell">{{ corte }}</td>
                    }
                  </tr>

                  @for (producto of bloque.productos; track $index) {
                    <tr
                      class="producto-row"
                      [class.clickable]="!!producto.url"
                      (click)="openProducto(producto.url)"
                    >
                      <td class="producto-cell">{{ producto.nombre }}</td>
                      @for (corte of cortesColumnas; track corte) {
                        <td class="precio-cell">{{ producto.cortes[corte] ?? '-' }}</td>
                      }
                    </tr>
                  }
                }
              </tbody>
            </table>
          </div>
        } @else {
          <p>No hay datos de precios para esta subasta.</p>
        }
      </section>
    </article>
  `,
  styleUrls: ['./details.css'],
})
export class Details {
  private readonly changeDetectorRef = inject(ChangeDetectorRef);
  private readonly route = inject(ActivatedRoute);
  private readonly subastasService = inject(SubastasService);
  private fechas: string[] = [];
  private fechaIndex = 0;
  private familias: Familia[] = [];
  private productos: Producto[] = [];
  private preciosSubasta: PrecioSubasta[] = [];

  subasta: Subasta | undefined;
  fechaSeleccionada = '';
  tablaFamilias: FamiliaTabla[] = [];
  cortesColumnas: number[] = Array.from({length: 10}, (_, index) => index + 1);

  get fechasDisponibles(): string[] {
    return this.fechas;
  }

  constructor() {
    const subastaId = parseInt(this.route.snapshot.params['id'], 10);
    this.loadSubasta(subastaId);
  }

  canGoPrev(): boolean {
    return this.fechaIndex > 0;
  }

  canGoNext(): boolean {
    return this.fechaIndex < this.fechas.length - 1;
  }

  changeDate(step: number) {
    const nextIndex = this.fechaIndex + step;
    if (nextIndex < 0 || nextIndex >= this.fechas.length) {
      return;
    }

    this.fechaIndex = nextIndex;
    this.fechaSeleccionada = this.fechas[this.fechaIndex] ?? '';
    this.buildTablaPrecios();
    this.changeDetectorRef.markForCheck();
  }

  openProducto(url: string | null) {
    if (!url) {
      return;
    }
    window.open(url, '_blank', 'noopener,noreferrer');
  }

  private async loadSubasta(subastaId: number) {
    try {
      const [subasta, familias, productos, preciosSubasta] = await Promise.all([
        this.subastasService.getSubastaById(subastaId),
        this.subastasService.getFamilias(),
        this.subastasService.getProductos(),
        this.subastasService.getPreciosBySubasta(subastaId),
      ]);

      this.subasta = subasta;
      this.familias = familias;
      this.productos = productos;
      this.preciosSubasta = preciosSubasta;
      this.fechas = [...new Set(preciosSubasta.map((precio) => precio.fecha))].sort();
      this.fechaIndex = Math.max(this.fechas.length - 1, 0);
      this.fechaSeleccionada = this.fechas[this.fechaIndex] ?? '';

      this.buildTablaPrecios();
      this.changeDetectorRef.markForCheck();
    } catch {
      this.subasta = undefined;
      this.familias = [];
      this.productos = [];
      this.preciosSubasta = [];
      this.fechas = [];
      this.fechaIndex = 0;
      this.fechaSeleccionada = '';
      this.tablaFamilias = [];
      this.changeDetectorRef.markForCheck();
    }
  }

  private buildTablaPrecios() {
    if (!this.preciosSubasta.length || !this.fechaSeleccionada) {
      this.tablaFamilias = [];
      return;
    }

    const productoById = new Map<number, Producto>(
      this.productos.map((producto) => [producto.id, producto]),
    );
    const familiaById = new Map<number, string>(
      this.familias.map((familia) => [familia.id, familia.nombre]),
    );

    const preciosFecha = this.preciosSubasta
      .filter((precio) => precio.fecha === this.fechaSeleccionada)
      .sort((a, b) => a.producto_id - b.producto_id || a.corte - b.corte);

    const productosMap = new Map<number, ProductoFila & {familiaId: number}>();
    for (const precio of preciosFecha) {
      const producto = productoById.get(precio.producto_id);
      const productoId = precio.producto_id;
      const existente = productosMap.get(productoId);

      if (existente) {
        existente.cortes[precio.corte] = precio.precio;
        continue;
      }

      productosMap.set(productoId, {
        nombre: producto?.nombre ?? `Producto ${productoId}`,
        url: producto?.url ?? null,
        familiaId: producto?.familia_id ?? -1,
        cortes: {
          [precio.corte]: precio.precio,
        },
      });
    }

    const familiaBlocks = new Map<number, FamiliaTabla>();
    for (const producto of productosMap.values()) {
      if (!familiaBlocks.has(producto.familiaId)) {
        familiaBlocks.set(producto.familiaId, {
          familia: familiaById.get(producto.familiaId) ?? '-',
          productos: [],
        });
      }

      familiaBlocks.get(producto.familiaId)?.productos.push({
        nombre: producto.nombre,
        url: producto.url,
        cortes: producto.cortes,
      });
    }

    const familiaOrder = new Map<number, number>(
      this.familias.map((familia, index) => [familia.id, index]),
    );

    this.tablaFamilias = [...familiaBlocks.entries()]
      .sort((a, b) => {
        const aOrder = familiaOrder.get(a[0]) ?? Number.MAX_SAFE_INTEGER;
        const bOrder = familiaOrder.get(b[0]) ?? Number.MAX_SAFE_INTEGER;
        return aOrder - bOrder;
      })
      .map(([, block]) => ({
        ...block,
        productos: block.productos.sort((a, b) => a.nombre.localeCompare(b.nombre)),
      }));
  }
}
