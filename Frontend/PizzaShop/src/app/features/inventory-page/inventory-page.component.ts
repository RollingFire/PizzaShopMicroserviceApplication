import { Component, OnInit } from '@angular/core';
import { InventoryApiCallerService } from 'src/app/core/services/inventory-api-caller/inventory-api-caller.service';
import { InventoryItem } from 'src/app/core/models/inventoryItem';

@Component({
  selector: 'app-inventory-page',
  templateUrl: './inventory-page.component.html',
  styleUrls: ['./inventory-page.component.scss']
})
export class InventoryPageComponent implements OnInit {
  inventory: InventoryItem[];
  rowEditItemId: number = null;

  constructor(private inventoryApiCallerService: InventoryApiCallerService) {
    inventoryApiCallerService.getInventory().subscribe(
      data => this.inventory = data,
      error => console.log(error)
    );
  }

  ngOnInit(): void {
  }

  openCreateNewInventoryItem(): void {
    document.getElementById("createNewInventoryItemButton").style.display = "none";
    document.getElementById("createNewInventoryItemForm").style.display = "";
  }

  closeCreateNewInventoryItem(): void {
    document.getElementById("createNewInventoryItemForm").style.display = "none";
    document.getElementById("createNewInventoryItemButton").style.display = "";
  }

  createInventoryItem(form): void {
    this.inventoryApiCallerService.createInventoryItem(form.value.name, form.value.units, form.value.unitType, form.value.restockAt, form.value.restockAmount).subscribe(
      data => this.inventory.push(data),
      error => console.log(error)
    );
  }

  openEditInventoryItem(id: number): void {
    this.rowEditItemId = id;
  }

  closeEditInventoryItem(): void {
    this.rowEditItemId = null;
  }

  submitEditInventoryItem(item: InventoryItem, form): void {
    console.log(form.value)
    this.inventoryApiCallerService.editInventoryItem(item.id, form.value.name, form.value.units, form.value.unitType, form.value.restockAt, form.value.restockAmount).subscribe(
      data => {
        let index = this.inventory.indexOf(item)
        this.inventory[index] = data
      },
      error => console.log(error)
    );
    this.closeEditInventoryItem()
  }

  deleteInventoryItem(item: InventoryItem): void {
    this.inventoryApiCallerService.deleteInventoryItem(item.id).subscribe(
      data => {
        let index = this.inventory.indexOf(item)
        this.inventory.splice(index, 1)
      },
      error => console.log(error)
    );
  }
}
