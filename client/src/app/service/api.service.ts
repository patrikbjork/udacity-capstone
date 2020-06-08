import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserInfo} from '../domain/user-info';

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
}
