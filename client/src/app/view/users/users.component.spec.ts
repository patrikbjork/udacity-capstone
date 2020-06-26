import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UsersComponent } from './users.component';
import {ApiService} from '../../service/api.service';
import {Observable, of} from 'rxjs';
import {AuthService} from '../../service/auth.service';
import {AppComponent} from '../../app.component';

describe('UsersComponent', () => {
  let component: UsersComponent;
  let fixture: ComponentFixture<UsersComponent>;
  let mockApiService: any;
  let mockAuthService: any;

  const dummyUserListResponse = [
    {sub: '1', name: 'Bluth', picture: 'https://url', email: 'e@ma.il'},
    {sub: '2', name: 'Weaver', picture: 'https://url', email: 'e@ma.il'},
    {sub: '3', name: 'Wong', picture: 'https://url', email: 'e@ma.il'},
  ];

  beforeEach(async(() => {
    mockApiService = jasmine.createSpyObj(['getAllUsers$']);
    mockApiService.getAllUsers$.and.returnValue(of(dummyUserListResponse));

    mockAuthService = {};
    mockAuthService.userProfile$ = of({sub: 'sub1'});

    TestBed.configureTestingModule({
      declarations: [ UsersComponent ],
      providers: [
        {provide: ApiService, useValue: mockApiService},
        {provide: AuthService, useValue: mockAuthService}
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render three app-profile-cards', () => {
    // const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.container').children.length).toEqual(3);
  });
});
