import { TestBed } from '@angular/core/testing';

import { BarcoService } from './barco.service';

describe('BarcoService', () => {
  let service: BarcoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BarcoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
