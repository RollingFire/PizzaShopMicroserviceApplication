import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './core/components/navbar/navbar.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { MenuComponent } from './features/menu/menu.component';
import { MenuSelectionTabsComponent } from './features/menu/components/menu-selection-tabs/menu-selection-tabs.component';
import { MenuDisplayComponent } from './features/menu/components/menu-display/menu-display.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomePageComponent,
    MenuComponent,
    MenuSelectionTabsComponent,
    MenuDisplayComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
