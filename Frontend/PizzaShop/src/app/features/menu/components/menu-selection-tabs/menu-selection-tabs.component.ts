import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-menu-selection-tabs',
  templateUrl: './menu-selection-tabs.component.html',
  styleUrls: ['./menu-selection-tabs.component.scss']
})
export class MenuSelectionTabsComponent implements OnInit {

  menus: any[] = [
    {
      "id": 0,
      "menuName": "Lunch",
      "items": "[1,2,3]",
      "revistionDate": "03-04-20"
    },
    {
      "id": 1,
      "menuName": "Dinner",
      "items": "[4,5,6]",
      "revistionDate": "03-10-20"
    }
  ];

  constructor() { }

  ngOnInit(): void {
  }

}
