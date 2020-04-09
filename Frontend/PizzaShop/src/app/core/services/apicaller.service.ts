import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class APICallerService {

  constructor(private http: HttpClient) {}

  getRequest(url): any {
    return this.http.get(url);
  }
}
