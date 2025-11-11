import { Routes } from '@angular/router';
import { BarcoListaComponent } from './barco/barco-lista/barco-lista.component';
import { BarcoViewComponent } from './barco/barco-view/barco-view.component';
import { BarcoEditComponent } from './barco/barco-edit/barco-edit.component';
import { BarcoCreateComponent } from './barco/barco-create/barco-create.component';
import { JugadorListaComponent } from './jugador/jugador-lista/jugador-lista.component';
import { JugadorViewComponent } from './jugador/jugador-view/jugador-view.component';
import { JugadorEditComponent } from './jugador/jugador-edit/jugador-edit.component';
import { JugadorCreateComponent } from './jugador/jugador-create/jugador-create.component';
import { MapaListaComponent } from './mapa/mapa-lista/mapa-lista.component';
import { MapaCreateComponent } from './mapa/mapa-create/mapa-create.component';
import { MapaViewComponent } from './mapa/mapa-view/mapa-view.component';
import { ModeloListaComponent } from './modelo/modelo-lista/modelo-lista.component';
import { ModeloCreateComponent } from './modelo/modelo-create/modelo-create.component';
import { ModeloViewComponent } from './modelo/modelo-view/modelo-view.component';
import { ModeloEditComponent } from './modelo/modelo-edit/modelo-edit.component';
import { PartidaMenuComponent } from './partida/partida-menu/partida-menu.component';
import { PartidaCrearComponent } from './partida/partida-crear/partida-crear.component';
import { PartidaJuegoComponent } from './partida/partida-juego/partida-juego.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './security/login/login.component';
import { SignupComponent } from './security/signup/signup.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';

export const routes: Routes = [
    // Rutas públicas - Login y Signup
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    
    // Ruta raíz - Home
    { path: '', component: HomeComponent },

    // Rutas de Barco - Solo ADMINISTRADOR
    { path: 'barco/list', component: BarcoListaComponent, canActivate: [adminGuard] },
    { path: 'barco/create', component: BarcoCreateComponent, canActivate: [adminGuard] },
    { path: 'barco/view/:id', component: BarcoViewComponent, canActivate: [adminGuard] },
    { path: 'barco/edit/:id', component: BarcoEditComponent, canActivate: [adminGuard] },

    // Rutas de Jugador - Solo ADMINISTRADOR
    { path: 'jugador/list', component: JugadorListaComponent, canActivate: [adminGuard] },
    { path: 'jugador/create', component: JugadorCreateComponent, canActivate: [adminGuard] },
    { path: 'jugador/view/:id', component: JugadorViewComponent, canActivate: [adminGuard] },
    { path: 'jugador/edit/:id', component: JugadorEditComponent, canActivate: [adminGuard] },

    // Rutas de Modelo - Solo ADMINISTRADOR
    { path: 'modelo/list', component: ModeloListaComponent, canActivate: [adminGuard] },
    { path: 'modelo/create', component: ModeloCreateComponent, canActivate: [adminGuard] },
    { path: 'modelo/view/:id', component: ModeloViewComponent, canActivate: [adminGuard] },
    { path: 'modelo/edit/:id', component: ModeloEditComponent, canActivate: [adminGuard] },

    // Rutas de Mapa - Solo ADMINISTRADOR
    { path: 'mapa/list', component: MapaListaComponent, canActivate: [adminGuard] },
    { path: 'mapa/create', component: MapaCreateComponent, canActivate: [adminGuard] },
    { path: 'mapa/view/:id', component: MapaViewComponent, canActivate: [adminGuard] },

    // Rutas de Partida - ADMINISTRADOR y JUGADOR (autenticados)
    // Crear/ver partidas: ambos roles pueden hacerlo
    // Jugar: el backend valida que solo JUGADOR puede hacer movimientos
    { path: 'partida/menu', component: PartidaMenuComponent, canActivate: [authGuard] },
    { path: 'partida/crear', component: PartidaCrearComponent, canActivate: [authGuard] },
    { path: 'partida/juego/:id', component: PartidaJuegoComponent, canActivate: [authGuard] }
];
