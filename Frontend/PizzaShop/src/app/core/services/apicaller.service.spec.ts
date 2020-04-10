import { TestBed } from '@angular/core/testing';

import { MenuAPICallerService } from './menuapicaller.service';

describe('APICallerService', () => {
  let service: MenuAPICallerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MenuAPICallerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
