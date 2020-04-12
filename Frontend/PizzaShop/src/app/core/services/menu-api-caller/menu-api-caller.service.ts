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

  getMenuItemById(id): Observable<MenuItem> {
    return this.http.get<MenuItem>("/api/menuItem/" + id);
  }
}
