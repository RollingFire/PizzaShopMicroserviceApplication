import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Menu } from '../models/menu';
import { MenuItem } from '../models/menuItem'


@Injectable({
  providedIn: 'root'
})
export class MenuAPICallerService {

  constructor(private http: HttpClient) {}

  getMenus(): any {
    return this.http.get<Menu[]>("/api/menu");
  }

  getMenuItemById(id): any {
    return this.http.get<MenuItem>("/api/menuItem" + id);
  }
}
