import { TestBed } from '@angular/core/testing';

import { MenuAPICallerService } from './menu-api-caller.service';

describe('MenuAPICallerService', () => {
  let service: MenuAPICallerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MenuAPICallerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
