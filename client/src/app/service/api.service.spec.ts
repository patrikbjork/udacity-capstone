import {getTestBed, TestBed} from '@angular/core/testing';

import { ApiService } from './api.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';

describe('ApiService', () => {
  let service: ApiService;
  let injector: TestBed;
  let httpMock: HttpTestingController;

  const dummyUserListResponse = [
    {sub: '1', name: 'Bluth', picture: 'https://url', email: 'e@ma.il'},
    {sub: '2', name: 'Weaver', picture: 'https://url', email: 'e@ma.il'},
    {sub: '3', name: 'Wong', picture: 'https://url', email: 'e@ma.il'},
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });

    injector = getTestBed();
    service = TestBed.inject(ApiService);
    httpMock = injector.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getAllUsers$() should return users', () => {
    service.getAllUsers$().subscribe((res) => {
      expect(res).toEqual(dummyUserListResponse);
    });

    const req = httpMock.expectOne('/api/secure/users');
    expect(req.request.method).toBe('GET');
    req.flush(dummyUserListResponse);
  });

  afterEach(() => {
    httpMock.verify();
  });
});
