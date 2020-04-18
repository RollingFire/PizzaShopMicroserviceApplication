import { Component, OnInit, Input } from '@angular/core';
import { MenuAPICallerService } from 'src/app/core/services/menu-api-caller/menu-api-caller.service';
import { FullMenu } from '../../menu.component';
import { MenuItem } from 'src/app/core/models/menuItem';
import { CookieCaller } from 'src/app/core/utillities/CookieCaller';


@Component({
  selector: 'app-menu-display',
  templateUrl: './menu-display.component.html',
  styleUrls: ['./menu-display.component.scss']
})
export class MenuDisplayComponent implements OnInit {
  @Input() currentMenu: FullMenu;
  allMenuItems: MenuItem[];
  addMenuItemSelected: MenuItem
  isAdmin: boolean;
  
  constructor(private apiCallerService: MenuAPICallerService) {
    apiCallerService.getMenuItems().subscribe(
      data => this.allMenuItems = data,
      error => console.log(error)
    );
    this.isAdmin = CookieCaller.getCookieValue("isAdmin") == "true"
  }
  
  ngOnInit(): void {
  }

  getCatagories(): Array<string> {
    return Array.from(this.currentMenu.menuItems.keys());
  }

  /**
   * Button to switch menu item to edit mode
   * @param elementId
   */
  editItem(elementId: string) {
    document.getElementById(elementId).style.display = "none";
    document.getElementById(elementId + "_edit").style.display = "";
  }

  /**
   * Button to switch menu item to non edit mode.
   * @param elementId 
   */
  editItemClose(elementId: string): void {
    document.getElementById(elementId + "_edit").style.display = "none";
    document.getElementById(elementId).style.display = "";
  }

  /**
   * 
   * @param id Menu item id
   * @param form Form of data to update the menu item with.
   */
  updateMenuItem(id: number, form) : void {
    // TODO only adding to changes if changed
    let changes: Map<string,string> = new Map<string,string>();
    if (form.value.itemName != "") {
      changes.set("itemName", form.value.itemName);
    }
    if (form.value.cost != "") {
      changes.set("cost", form.value.cost);
    }
    if (form.value.discription != "") {
      changes.set("discription", form.value.discription);
    }
    this.apiCallerService.updateManuItemById(id, changes).subscribe(
      data => this.updateDisplayMenuItem(data),
      error => console.log(error)
    );
  }

  /**
   * Updates the display of the menuItem after edited.
   * @param menuItem Menu item to update the display to.
   */
  updateDisplayMenuItem(menuItem: MenuItem): void {
    for (let i = 0; i < this.currentMenu.menuItems.get(menuItem.catagory).length; i++) {
      if (this.currentMenu.menuItems.get(menuItem.catagory)[i].id == menuItem.id) {
        let items: MenuItem[] = this.currentMenu.menuItems.get(menuItem.catagory);
        items[i] = menuItem;
        this.currentMenu.menuItems.set(menuItem.catagory, items);
        break;
      }
    }
  }

  /**
   * Removes a menu item from a menu.
   * @param menuid menuId where the menuItem was removed
   * @param menuItemId menuItemId of the menuItem to remove
   * @param catagory catagory where the item is removed
   */
  removeMenuItem(menuId: number, menuItemId: number, catagory: string): void {
    let index = this.currentMenu.menu.items.indexOf(menuItemId);
    this.currentMenu.menu.items.splice(index, 1);
    this.apiCallerService.updateMenuById(menuId, this.currentMenu.menu.items).subscribe(
      data => this.removeDisplyMenuItem(menuItemId, catagory),
      error => console.log(error)
    );
  }

  /**
   * Update the display after the menuItem is removed.
   * @param id menuItemId
   * @param catagory catagory where the menuItem was removed
   */
  removeDisplyMenuItem(id: number, catagory: string): void {
    for (let i = 0; i < this.currentMenu.menuItems.get(catagory).length; i++) {
      if (this.currentMenu.menuItems.get(catagory)[i].id == id) {
        let items: MenuItem[] = this.currentMenu.menuItems.get(catagory)
        items.splice(i, 1)
        if (items.length == 0) {
          this.currentMenu.menuItems.delete(catagory)
        } else {
          this.currentMenu.menuItems.set(catagory, items)
        }
        break;
      }
    }
  }
  
  /**
   * Add a menuItem to a menu.
   * @param menuId Menu id where item is added
   * @param newItem New menutItem to be added to the menu
   */
  addMenuItemToMenu(menuId: number, newItem: MenuItem) {
    let oldItems = this.currentMenu.menu.items;
    if (oldItems.indexOf(newItem.id) == -1) {
      oldItems.push(newItem.id)
      this.apiCallerService.updateMenuById(menuId, oldItems).subscribe(
        data => this.addMenuItemToDisplay(newItem),
        error => console.log(error)
      );
    }
  }

  /**
   * Update display to reflect newly added menuItem
   * @param newItem Newly added menutItem to also display
   */
  addMenuItemToDisplay(newItem: MenuItem) {
    let items: MenuItem[] = this.currentMenu.menuItems.get(newItem.catagory) || []
    items.push(newItem)
    this.currentMenu.menuItems.set(newItem.catagory, items)
  }

  /**
   * Show the add menuItem section
   */
  openAddMenuItemToMenu() {
    document.getElementById("addMenuItemToMenu").style.display = "";
    document.getElementById("menuItemAddCreateButtons").style.display = "none";
  }

  /**
   * Hide the add menuItem section
   */
  closeAddMenuItemToMenu() {
    document.getElementById("menuItemAddCreateButtons").style.display = "";
    document.getElementById("addMenuItemToMenu").style.display = "none";
  }

  /**
   * Shows the create menu item section
   */
  openCreateMenuItem() {
    document.getElementById("createMenuItem").style.display = "";
    document.getElementById("menuItemAddCreateButtons").style.display = "none";
  }

  /**
   * Closes the create menu item section
   */
  closeCreateMenuItem() {
    document.getElementById("menuItemAddCreateButtons").style.display = "";
    document.getElementById("createMenuItem").style.display = "none";
  }

  /**
   * Creates a new menuItem and adds it to the current menu.
   */
  createAndAddNewMenuItem(menuId: number, form) {
    this.apiCallerService.createNewMenuItem(form.value.catagory, form.value.itemName, form.value.cost, form.value.discription).subscribe(
      data => this.addMenuItemToMenu(menuId, data),
      error => console.log(error)
    );
  }
}
