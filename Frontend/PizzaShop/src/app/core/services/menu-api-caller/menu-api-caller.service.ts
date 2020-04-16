import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Menu } from '../../models/Menu';
import { MenuItem } from '../../models/MenuItem'
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MenuAPICallerService {

  constructor(private http: HttpClient) {
  }

  getMenus(): Observable<Menu[]> {
    return this.http.get<Menu[]>("/api/menu");
  }

  getMenuItems(): Observable<MenuItem[]> {
    return this.http.get<MenuItem[]>("/api/menuItem");
  }

  getMenuItemById(id: number): Observable<MenuItem> {
    return this.http.get<MenuItem>("/api/menuItem/" + id);
  }

  updateMenuById(id: number, items: number[]): Observable<Menu> {
    return this.http.put<Menu>("/api/menu/" + id, {"items": items});
  }

  updateManuItemById(id: number, changes: Map<string, string>): Observable<MenuItem> {
    let queryParm: string = "";
    let keys = Array.from(changes.keys());
    let values = Array.from(changes.values());
    for (let i = 0; i < changes.size; i++) {
      if (changes.values[i] == "") {
        continue
      }
      if (queryParm == "") {
        queryParm = "?"
      } else {
        queryParm += "&"
      }
      queryParm += keys[i] + "=" + values[i];
    }
    return this.http.put<MenuItem>("/api/menuItem/" + id + queryParm, "");
  }

  createNewMenu(items: number[]): Observable<Menu> {
    return this.http.post<Menu>("/api/menu", {"items": items});
  }

  createNewMenuItem(catagory: string, itemName: string, cost: number, discription: string): Observable<MenuItem> {
    return this.http.post<MenuItem>("/api/menuItem", {"catagory":catagory, "itemName":itemName, "discription":discription, "cost":cost});
  }
}
