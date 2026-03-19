import {Routes} from '@angular/router';
import {Subastas} from './subastas/subastas';
import {Details} from './details/details';
const routeConfig: Routes = [
  {
    path: '',
    component: Subastas,
    title: 'Subastas',
  },
  {
    path: 'details/:id',
    component: Details,
    title: 'Detalle de subasta',
  },
];
export default routeConfig;