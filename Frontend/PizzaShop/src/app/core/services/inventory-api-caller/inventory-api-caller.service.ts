import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InventoryItem } from '../../models/inventoryItem'
import { HttpClient } from '@angular/common/http';
import { strict } from 'assert';

@Injectable({
  providedIn: 'root'
})
export class InventoryApiCallerService {

  constructor(private http: HttpClient) { }

  getInventory(): Observable<InventoryItem[]> {
    return this.http.get<InventoryItem[]>("/api/inventory");
  }

  createInventoryItem(name: string, units: number, unitType: string, restockAt: number, restockAmount: number): Observable<InventoryItem> {
    return this.http.post<InventoryItem>("/api/inventory", {"name": name, "units": units, "unitType": unitType, "restockAt": restockAt, "restockAmount": restockAmount});
  }
  
  deleteInventoryItem(id: number): Observable<String> {
    return this.http.delete<String>("/api/inventory/" + id);
  }
  
  editInventoryItem(id: number, name: string, units: number, unitType: string, restockAt: number, restockAmount: number): Observable<InventoryItem> {
    console.log({"name": name, "units": units, "unitType": unitType, "restockAt": restockAt, "restockAmount": restockAmount})
    return this.http.put<InventoryItem>("/api/inventory/" + id, {"name": name, "units": units, "unitType": unitType, "restockAt": restockAt, "restockAmount": restockAmount});
  }
}
