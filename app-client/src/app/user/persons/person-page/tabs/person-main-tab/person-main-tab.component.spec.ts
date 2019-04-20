import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonMainTabComponent } from './person-main-tab.component';

describe('PersonMainTabComponent', () => {
  let component: PersonMainTabComponent;
  let fixture: ComponentFixture<PersonMainTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonMainTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonMainTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
