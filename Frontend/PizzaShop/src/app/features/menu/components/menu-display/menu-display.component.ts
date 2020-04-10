import { Component, OnInit, Input } from '@angular/core';
import { MenuAPICallerService } from 'src/app/core/services/menuapicaller.service';
import { Menu } from 'src/app/core/models/menu';


@Component({
  selector: 'app-menu-display',
  templateUrl: './menu-display.component.html',
  styleUrls: ['./menu-display.component.scss']
})
export class MenuDisplayComponent implements OnInit {
  @Input() currentMenu: Menu;

  constructor(private apiCallerService: MenuAPICallerService) {
    // this.currentMenu.items
    // apiCallerService.getMenuItemById().subscribe(
    //   data => this.menu = {
    //     "itemName": data.itemName,
    //     "cost": data.cost,
    //     "discription": data.discription
    //   },
    //   error => console.log(error)
    // );
  }

  ngOnInit(): void {
  }
}
