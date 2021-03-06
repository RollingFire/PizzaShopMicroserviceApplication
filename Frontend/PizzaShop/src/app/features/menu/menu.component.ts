import { Component, OnInit } from '@angular/core';
import { MenuAPICallerService } from 'src/app/core/services/menu-api-caller/menu-api-caller.service';
import { Menu } from 'src/app/core/models/menu';
import { MenuItem } from 'src/app/core/models/menuItem';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  menus: Menu[];
  currentMenu: FullMenu = {
    menu: {
      id: 0,
      menuName: "",
      items: [],
      revisionDate: ""
    },
    menuItems: new Map<string, MenuItem[]>()
  };
  private menuItems: Map<string, MenuItem[]>;

  constructor(private menuApiCallerService: MenuAPICallerService) {
    this.menuItems = new Map<string, MenuItem[]>()
    menuApiCallerService.getMenus().subscribe(
      data => {this.menus = data, this.buildFullMenu(data[0])},
      error => console.log(error),
    );
  }

  ngOnInit(): void {
  }

  /**
   * Changes the selected menu.
   * @param menuID Id of the new menu selected
   */
  changeMenu(menuID): void {
    let newMenu: Menu
    this.menus.forEach( function(menu) {
      if (menu.id === menuID) {
        newMenu = menu;
      }
    })
    this.buildFullMenu(newMenu)
  }

  /**
   * Builds the this.currentMenu with the menu and menuItems for the passed menu.
   * @param menu Menu to build
   */
  buildFullMenu(menu: Menu): void {
    this.menuItems = new Map<string, MenuItem[]>()
    for (let i = 0; i < menu.items.length; i++) {
      this.menuApiCallerService.getMenuItemById(menu.items[i]).subscribe(
        data => {
          let list = this.getMapValue(this.menuItems, data.catagory);
          list.push(data);
          this.menuItems.set(data.catagory, list);
        },
        error => console.log(error)
      );
    }
    this.currentMenu = {
      menu: menu,
      menuItems: this.menuItems
    };
  }

  /**
   * Proviteds a default map. Returns an emprty array if the key is undefined.
   * @param map Map to get from
   * @param key Key used in get
   */
  getMapValue(map, key) {
    return map.get(key) || [];
  }
}

export interface FullMenu {
  menu: Menu;
  menuItems: Map<string, MenuItem[]>;
}
