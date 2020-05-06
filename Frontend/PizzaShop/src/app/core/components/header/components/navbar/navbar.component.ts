import { Component, OnInit } from '@angular/core';
import { CookieCaller } from '../../../../utillities/CookieCaller';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit {
  isAdmin: boolean;
  displayTabs: any[] = [];
  private allTabs: any[] = [
    {"name": "Home",      "href": "/",          "adminOnlyTab": false},
    {"name": "Menu",      "href": "/menu",      "adminOnlyTab": false},
    {"name": "Inventory", "href": "/inventory", "adminOnlyTab": true},
  ];

  constructor() {
    this.isAdmin = CookieCaller.getCookieValue("isAdmin") == "true"
    for (let tab of this.allTabs) {
      if (!tab.adminOnlyTab || this.isAdmin) {
        this.displayTabs.push(tab)
      }
    }
  }

  ngOnInit(): void {}


  isActive(path) {
    return (location.pathname === path) ? 'active' : null;
  }
}
