import { Routes } from '@angular/router';
import { BarcoListaComponent } from './barco/barco-lista/barco-lista.component';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';

export const routes: Routes = [
    { path: 'barco/list', component: BarcoListaComponent },
    { path: 'barco/view/:id', component: BarcoViewComponent },
    { path: 'barco/edit/:id', component: BarcoEditComponent },
    { path: '', redirectTo: 'barco/list', pathMatch: 'full' }
];
