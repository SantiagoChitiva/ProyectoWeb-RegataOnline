import { TestBed } from '@angular/core/testing';

import { CeldaService } from './celda.service';

describe('CeldaService', () => {
  let service: CeldaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CeldaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
