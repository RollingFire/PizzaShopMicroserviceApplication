import { Component, OnInit } from '@angular/core';
import { APICallerService } from 'src/app/core/services/apicaller.service';

@Component({
  selector: 'app-menu-display',
  templateUrl: './menu-display.component.html',
  styleUrls: ['./menu-display.component.scss']
})
export class MenuDisplayComponent implements OnInit {

  menu: any;
  
  constructor(private apiCallerService: APICallerService) {
    apiCallerService.getRequest("http://localhost:9095/menuItem/1").subscribe(
      data => console.log(data),
      error => console.log(error)
    );
  }

  ngOnInit(): void {}
}
