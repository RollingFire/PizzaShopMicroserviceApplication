import { TestBed } from '@angular/core/testing';

import { APICallerService } from './apicaller.service';

describe('APICallerService', () => {
  let service: APICallerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(APICallerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
