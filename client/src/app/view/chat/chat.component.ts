import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../service/auth.service';
import {ChatMessage} from '../../domain/chat-message';
import {ApiService} from '../../service/api.service';
import {ServerChatMessage} from '../../domain/server-chat-message';
import {Observable} from 'rxjs';
import {UserInfo} from '../../domain/user-info';
import {map} from 'rxjs/operators';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  chatWebSocket: WebSocket;

  chatMessages: ChatMessage[] = [];
  currentText: string;
  thisUserId: string;
  private otherUserId: string;
  otherUsersName: Observable<string>;

  constructor(private activatedRoute: ActivatedRoute,
              private auth: AuthService,
              public api: ApiService) { }

  ngOnInit(): void {
    this.otherUserId = this.activatedRoute.snapshot.params.userId;

    this.otherUsersName = this.api.getUser(encodeURI(this.activatedRoute.snapshot.params.userId)).pipe(
      map((userInfo: UserInfo) => userInfo.name)
    );

    this.auth.userProfile$.subscribe((userProfile: any) => {
      if (!userProfile) {
        return;
      }

      this.thisUserId = userProfile.sub;

      this.api.getMyConversation(encodeURI(this.otherUserId)).subscribe((serverChatMessages: ServerChatMessage[]) => {
        this.chatMessages = serverChatMessages.map(m => {
          return JSON.parse(m.data) as ChatMessage;
        });
      });

      this.initWebsocket();
      // const pingWebSocket = new WebSocket(protocol + '://' + window.location.host + '/ping/' + otherUserId);
      // pingWebSocket.onopen = () => pingWebSocket.send('ping');
      this.chatWebSocket.onopen = () => {
        console.log('onopen');
        // setTimeout(() => this.chatWebSocket.send('ping'), 10000);
        // this.chatWebSocket.send('ping');
      };
    });
  }

  private initWebsocket(): void {
    const protocol = window.location.protocol === 'https' ? 'wss' : 'ws';
    this.chatWebSocket = new WebSocket(environment.webSocketBaseUrl
      + '/chat/' + encodeURI(this.thisUserId) + '/' + encodeURI(this.otherUserId));

    this.chatWebSocket.onmessage = (messageEvent: MessageEvent) => {
      const chatMessage = JSON.parse(messageEvent.data);
      this.chatMessages.push(chatMessage);
    };

    this.chatWebSocket.onclose = (e: CloseEvent) => {
      setTimeout(() => this.initWebsocket());
    };
  }


  sendMessage(): void {
    // console.log('sending: ' + this.currentText);
    const chatMessage = {sender: this.thisUserId, recipient: this.otherUserId, text: this.currentText};
    this.chatMessages.push(chatMessage);
    this.chatWebSocket.send(JSON.stringify(chatMessage));
    this.currentText = '';
  }
}
