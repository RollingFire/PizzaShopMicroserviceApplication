import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Menu } from 'src/app/core/models/menu';
import { MenuAPICallerService } from 'src/app/core/services/menu-api-caller/menu-api-caller.service';
import { CookieCaller } from 'src/app/core/utillities/CookieCaller';

@Component({
  selector: 'app-menu-selection-tabs',
  templateUrl: './menu-selection-tabs.component.html',
  styleUrls: ['./menu-selection-tabs.component.scss']
})
export class MenuSelectionTabsComponent implements OnInit {
  @Input() menus: Menu[];
  @Output() tabClicked = new EventEmitter<number>();
  isAdmin: boolean;

  constructor(private apiCallerService: MenuAPICallerService) {
    this.isAdmin = CookieCaller.getCookieValue("isAdmin") == "true"
  }

  ngOnInit(): void {
  }

  clickTab(id: number): void {
    this.tabClicked.emit(id);
  }

  /**
   * Creates a new menu from the from and selects it.
   * @param form Create menu form
   */
  createMenu(form) {
    this.apiCallerService.createNewMenu(form.value.menuName, []).subscribe(
      data => {
        this.menus.push(data),
        this.clickTab(data.id)
      },
      error => console.log(error)
    );
  }

  /**
   * Open the create new menu form
   */
  openMenuCreate() {
    document.getElementById("createMenuForm").style.display = ""
    document.getElementById("createMenu").style.display = "none"
  }

  /**
   * Closes the create new menu form
   */
  closeMenuCreate() {
    document.getElementById("createMenu").style.display = ""
    document.getElementById("createMenuForm").style.display = "none"
  }
}
