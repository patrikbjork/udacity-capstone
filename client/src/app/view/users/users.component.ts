import { Component, OnInit } from '@angular/core';
import {UserInfo} from '../../domain/user-info';
import {Observable} from 'rxjs';
import {ApiService} from '../../service/api.service';
import {AuthService} from '../../service/auth.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

  users$: Observable<UserInfo[]>;

  constructor(private api: ApiService,
              public auth: AuthService) { }

  ngOnInit(): void {
    this.users$ = this.api.getAllUsers$();
  }

}
