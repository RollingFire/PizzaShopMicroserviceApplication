import { Component, OnInit } from '@angular/core';
import { CookieCaller } from 'src/app/core/utillities/CookieCaller';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.scss']
})
export class LoginPageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  submitLoginForm(form) {
    CookieCaller.setCookieValue("user", form.value.userName)
    CookieCaller.setCookieValue("isAdmin", form.value.isAdmin)
    let originPath: string = CookieCaller.getCookieValue("originPath")
    CookieCaller.expireCookieValue("originPath")
    if (originPath == null) {
      originPath = "/"
    }
    document.location.pathname = originPath
  }
}
