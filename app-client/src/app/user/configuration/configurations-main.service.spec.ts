import { TestBed } from '@angular/core/testing';

import { ConfigurationsMainService } from './configurations-main.service';

describe('ConfigurationsMainService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConfigurationsMainService = TestBed.get(ConfigurationsMainService);
    expect(service).toBeTruthy();
  });
});
