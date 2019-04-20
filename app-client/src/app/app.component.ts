import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {

  sideNavItems = [{
    name: 'Сотрудники',
    link: 'persons',
  }, {
    name: 'Настройки',
    link: 'configurations',
  }];
}
