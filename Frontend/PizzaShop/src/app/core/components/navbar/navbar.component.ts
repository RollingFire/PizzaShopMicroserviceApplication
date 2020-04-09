import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})

export class NavbarComponent implements OnInit {

  constructor() {}

  ngOnInit(): void {}

  private tabs: any[] = [
    {"name": "Home",      "href": "/"},
    {"name": "Menu",      "href": "/menu"},
    {"name": "Inventory", "href": "/inventory", "adminTab": true}
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
