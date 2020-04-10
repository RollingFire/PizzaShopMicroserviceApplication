import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Menu } from 'src/app/core/models/menu';

@Component({
  selector: 'app-menu-selection-tabs',
  templateUrl: './menu-selection-tabs.component.html',
  styleUrls: ['./menu-selection-tabs.component.scss']
})
export class MenuSelectionTabsComponent implements OnInit {
  @Input() menus: Menu[];
  @Output() tabClicked = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  clickTab(id: number): void {
    this.tabClicked.emit(id);
  }
}
