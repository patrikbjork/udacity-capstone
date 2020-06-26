import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import {of} from 'rxjs';
import {ApiService} from './service/api.service';
import {AuthService} from './service/auth.service';
import {MatMenuModule} from '@angular/material/menu';

describe('AppComponent', () => {
  let mockApiService: any;
  let mockAuthService: any;

  beforeEach(async(() => {
    mockApiService = jasmine.createSpyObj(['saveUser$']);
    mockApiService.saveUser$.and.returnValue(of({}));

    mockAuthService = {};
    mockAuthService.userProfile$ = of({sub: 'sub1'});

    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatMenuModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        {provide: ApiService, useValue: mockApiService},
        {provide: AuthService, useValue: mockAuthService}
      ]
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  /*it(`should have as title 'client'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('client');
  });*/

  it('should render header', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('#container #header')).toBeTruthy();
  });
});
