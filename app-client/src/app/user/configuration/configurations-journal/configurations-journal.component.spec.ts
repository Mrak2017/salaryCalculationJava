import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfigurationsJournalComponent } from './configurations-journal.component';

describe('ConfigurationsJournalComponent', () => {
  let component: ConfigurationsJournalComponent;
  let fixture: ComponentFixture<ConfigurationsJournalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfigurationsJournalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfigurationsJournalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
