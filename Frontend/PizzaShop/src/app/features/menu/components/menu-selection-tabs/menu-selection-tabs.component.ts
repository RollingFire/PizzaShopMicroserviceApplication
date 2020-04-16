import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Menu } from 'src/app/core/models/Menu';
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

  createMenu(form) {
    this.apiCallerService.createNewMenu(form.value.menuName, []).subscribe(
      data => {
        this.menus.push(data),
        this.clickTab(data.id)
      },
      error => console.log(error)
    );
  }

  openMenuCreate() {
    document.getElementById("createMenuForm").style.display = ""
    document.getElementById("createMenu").style.display = "none"
  }

  closeMenuCreate() {
    document.getElementById("createMenu").style.display = ""
    document.getElementById("createMenuForm").style.display = "none"
  }
}
