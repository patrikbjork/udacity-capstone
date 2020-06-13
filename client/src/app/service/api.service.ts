import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserInfo} from '../domain/user-info';
import {ChatMessage} from '../domain/chat-message';
import {ServerChatMessage} from '../domain/server-chat-message';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient) { }

  getAllUsers$(): Observable<UserInfo[]> {
    return this.http.get<UserInfo[]>('/api/secure/users');
  }

  saveUser$(): Observable<any> {
    return this.http.post('/api/secure/users/save-user', {});
  }

  getMyConversation(otherUserId: string): Observable<ServerChatMessage[]> {
    return this.http.get<ServerChatMessage[]>('/api/secure/messages/my-messages/' + otherUserId);
  }
}
