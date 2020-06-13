import { Component } from '@angular/core';
import {AuthService} from './service/auth.service';
import {ApiService} from './service/api.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'client';

  webSocket: WebSocket;

  pingByUserId: string;
  pingByUserName: string;

  constructor(public auth: AuthService,
              private api: ApiService,
              private router: Router) {
    this.auth.userProfile$.subscribe(value => {
      console.log('getUser: ' + value);
      if (value) {
        api.saveUser$().subscribe(_ => console.log('Saved user.'));

        if (this.webSocket) {
          this.webSocket.close();
        }

        const protocol = window.location.protocol === 'https' ? 'wss' : 'ws';
        this.webSocket = new WebSocket('ws://localhost:8080' + '/ping/' + value.sub);

        this.webSocket.onmessage = (messageEvent: MessageEvent) => {
          const split = (messageEvent.data as string).split(':');
          const otherUserId = split[0];

          if (this.router.url !== '/chat/' + encodeURI(otherUserId)) {
            this.pingByUserId = otherUserId;
            this.pingByUserName = split[1];
          }
        };
      }
    });
  }
}
