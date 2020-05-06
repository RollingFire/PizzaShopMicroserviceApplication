import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomePageComponent } from './features/home-page/home-page.component';
import { MenuComponent } from './features/menu/menu.component';
import { LoginPageComponent } from './features/login-page/login-page.component'
import { InventoryPageComponent } from './features/inventory-page/inventory-page.component';


const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'menu', component: MenuComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'inventory', component: InventoryPageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
