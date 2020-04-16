import { Component, OnInit } from '@angular/core';
import { CookieCaller } from 'src/app/core/utillities/CookieCaller';

@Component({
  selector: 'app-header-user-section',
  templateUrl: './header-user-section.component.html',
  styleUrls: ['./header-user-section.component.scss']
})
export class HeaderUserSectionComponent implements OnInit {
  user: string;
  isLoginPage: boolean;

  constructor() {
    this.user = CookieCaller.getCookieValue("user")
    this.isLoginPage = location.pathname === "/login"
  }

  ngOnInit(): void {
  }

  login(): void {
    CookieCaller.setCookieValue("originPath", document.location.pathname)
    document.location.pathname = "/login"
  }

  logout(): void {
    CookieCaller.expireCookieValue("user")
    CookieCaller.expireCookieValue("isAdmin")
    document.location.reload()
  }
}
