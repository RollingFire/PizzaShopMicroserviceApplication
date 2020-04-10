import { Component, OnInit } from '@angular/core';
import { MenuAPICallerService } from 'src/app/core/services/menuapicaller.service';
import { Menu } from 'src/app/core/models/menu';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  menus: Menu[];
  currentMenu: Menu;

  constructor(private menuApiCallerService: MenuAPICallerService) {
    menuApiCallerService.getMenus().subscribe(
      data => {this.menus = data, this.currentMenu = data[0]},
      error => console.log(error),
    );
  }

  ngOnInit(): void {
  }

  changeMenu(menuID): void {
    var newMenuSelection;
    this.menus.forEach( function(menu) {
      if (menu.id === menuID) {
        newMenuSelection = menu;
      }
    })
    this.currentMenu = newMenuSelection
  }
}
