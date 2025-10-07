import { Routes } from '@angular/router';
import { BarcoListaComponent } from './barco/barco-lista/barco-lista.component';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';
import { JugadorListaComponent } from './jugador/jugador-lista/jugador-lista.component';
import { JugadorViewComponent } from './jugador/jugador-view/jugador-view.component';
import { JugadorEditComponent } from './jugador/jugador-edit/jugador-edit.component';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'barco/list', component: BarcoListaComponent },
    { path: 'barco/view/:id', component: BarcoViewComponent },
    { path: 'barco/edit/:id', component: BarcoEditComponent },
    { path: 'jugador/list', component: JugadorListaComponent },
    { path: 'jugador/view/:id', component: JugadorViewComponent },
    { path: 'jugador/edit/:id', component: JugadorEditComponent }
];
