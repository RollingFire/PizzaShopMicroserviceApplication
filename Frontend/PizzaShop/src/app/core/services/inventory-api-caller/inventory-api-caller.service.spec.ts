import { TestBed } from '@angular/core/testing';

import { InventoryApiCallerService } from './inventory-api-caller.service';

describe('InventoryApiCallerService', () => {
  let service: InventoryApiCallerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InventoryApiCallerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
