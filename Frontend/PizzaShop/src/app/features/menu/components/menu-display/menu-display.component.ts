import { Component, OnInit, Input } from '@angular/core';
import { MenuAPICallerService } from 'src/app/core/services/menu-api-caller/menu-api-caller.service';
import { FullMenu } from '../../menu.component';


@Component({
  selector: 'app-menu-display',
  templateUrl: './menu-display.component.html',
  styleUrls: ['./menu-display.component.scss']
})
export class MenuDisplayComponent implements OnInit {
  @Input() currentMenu: FullMenu;
  catagories: string[];

  constructor(private apiCallerService: MenuAPICallerService) {
  }
  
  ngOnInit(): void {
    this.catagories = Array.from(this.currentMenu.menuItems.keys());
  }
}
