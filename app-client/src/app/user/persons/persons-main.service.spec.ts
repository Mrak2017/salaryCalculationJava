import { TestBed } from '@angular/core/testing';

import { PersonsMainService } from './persons-main.service';

describe('PersonsMainService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PersonsMainService = TestBed.get(PersonsMainService);
    expect(service).toBeTruthy();
  });
});
