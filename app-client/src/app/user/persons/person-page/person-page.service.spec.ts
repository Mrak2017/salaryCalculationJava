import { TestBed } from '@angular/core/testing';

import { PersonPageService } from './person-page.service';

describe('PersonPageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: PersonPageService = TestBed.get(PersonPageService);
    expect(service).toBeTruthy();
  });
});
