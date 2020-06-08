import { Component } from '@angular/core';
import {AuthService} from './service/auth.service';
import {ApiService} from './service/api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'client';

  constructor(public auth: AuthService,
              private api: ApiService ) {
    this.auth.userProfile$.subscribe(value => {
      console.log('getUser: ' + value);
      if (value) {
        api.saveUser$().subscribe(_ => console.log('Saved user.'));
      }
    });
  }
}
