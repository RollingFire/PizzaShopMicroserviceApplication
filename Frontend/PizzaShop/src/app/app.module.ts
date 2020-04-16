import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './core/components/header/components/navbar/navbar.component';
import { HomePageComponent } from './features/home-page/home-page.component';
import { MenuComponent } from './features/menu/menu.component';
import { MenuSelectionTabsComponent } from './features/menu/components/menu-selection-tabs/menu-selection-tabs.component';
import { MenuDisplayComponent } from './features/menu/components/menu-display/menu-display.component';
import { HeaderUserSectionComponent } from './core/components/header/components/header-user-section/header-user-section.component';
import { HeaderComponent } from './core/components/header/header.component';
import { LoginPageComponent } from './features/login-page/login-page.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomePageComponent,
    MenuComponent,
    MenuSelectionTabsComponent,
    MenuDisplayComponent,
    HeaderUserSectionComponent,
    HeaderComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
