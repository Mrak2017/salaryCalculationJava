import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonGroupTabComponent } from './person-group-tab.component';

describe('PersonGroupTabComponent', () => {
  let component: PersonGroupTabComponent;
  let fixture: ComponentFixture<PersonGroupTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonGroupTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonGroupTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
