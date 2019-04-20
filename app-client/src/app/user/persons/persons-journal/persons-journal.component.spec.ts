import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonsJournalComponent } from './persons-journal.component';

describe('PersonsJournalComponent', () => {
  let component: PersonsJournalComponent;
  let fixture: ComponentFixture<PersonsJournalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonsJournalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonsJournalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
