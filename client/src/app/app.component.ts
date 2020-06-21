import { Component } from '@angular/core';
import {AuthService} from './service/auth.service';
import {ApiService} from './service/api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

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

        this.initWebSocket(value);
      }
    });
  }

  private initWebSocket(value: any) {
    this.webSocket = new WebSocket(environment.webSocketBaseUrl + '/ping/' + encodeURI(value.sub));

    this.webSocket.onmessage = (messageEvent: MessageEvent) => {
      const split = (messageEvent.data as string).split(':');
      const otherUserId = split[0];

      if (this.router.url !== '/chat/' + encodeURI(otherUserId)) {
        this.pingByUserId = otherUserId;
        this.pingByUserName = split[1];
      }
    };

    this.webSocket.onclose = () => {
      setTimeout(() => this.initWebSocket(value));
    };
  }

  resetPing() {
    this.pingByUserId = '';
    this.pingByUserName = '';
  }
}
