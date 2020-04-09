import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomePageComponent } from './features/home-page/home-page.component';
import { MenuComponent } from './features/menu/menu.component';


const routes: Routes = [
  { path: '', component: HomePageComponent},
  { path: 'menus', component: MenuComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
