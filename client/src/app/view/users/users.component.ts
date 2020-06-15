import { Component, OnInit } from '@angular/core';
import {UserInfo} from '../../domain/user-info';
import {Observable} from 'rxjs';
import {ApiService} from '../../service/api.service';
import {AuthService} from '../../service/auth.service';
import {tap} from 'rxjs/operators';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  users$: Observable<UserInfo[]>;

  onlineCheckWebSocket: WebSocket;

  usersOnlineStatus = new Map<string, boolean>();

  constructor(private api: ApiService,
              public auth: AuthService) { }

  ngOnInit(): void {
    this.users$ = this.api.getAllUsers$();

    this.auth.userProfile$.subscribe(userProfile => {
      if (!userProfile) {
        return;
      }
      this.initWebsocket(userProfile.sub);

      this.users$.pipe(
        tap(users => {
          users.forEach(user => {
            this.onlineCheckWebSocket.send(user.sub);
          });
        })
      ).subscribe();
    });

  }

  onMessage(messageEvent: MessageEvent): void {
    const split = messageEvent.data.text.split(':');
    this.usersOnlineStatus.set(split[0], split[1] === 'online');
  }

  private initWebsocket(thisUserId: string): void {
    const protocol = window.location.protocol === 'https' ? 'wss' : 'ws';
    this.onlineCheckWebSocket = new WebSocket(environment.webSocketBaseUrl + '/user-online-check/' + thisUserId);
    this.onlineCheckWebSocket.onmessage = (messageEvent: MessageEvent) => {
      const split = messageEvent.data.split(':');
      this.usersOnlineStatus.set(split[0], split[1] === 'online');
    };
  }

}
