import { Component, OnInit } from '@angular/core';
import { CookieCaller } from '../../utillities/CookieCaller';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit {
  isAdmin: boolean;

  constructor() {
    this.isAdmin = CookieCaller.getCookieValue("isAdmin") == "true"
  }

  ngOnInit(): void {}

  tabs: any[] = [
    {"name": "Home",      "href": "/"},
    {"name": "Menu",      "href": "/menu"},
  ];

  getTabs() {
    var tabsToShow = [];
    for (let i = 0; i < this.tabs.length; i++) {
      if (this.tabs[i]["adminTab"] != true) {
        tabsToShow.push(this.tabs[i]);
      }
    }
    return tabsToShow;
  }

  isActive(path) {
    return (location.pathname === path) ? 'active' : null;
  }
}
