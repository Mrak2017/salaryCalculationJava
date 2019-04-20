import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonOrgStructureTabComponent } from './person-org-structure-tab.component';

describe('PersonOrgStructureTabComponent', () => {
  let component: PersonOrgStructureTabComponent;
  let fixture: ComponentFixture<PersonOrgStructureTabComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PersonOrgStructureTabComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonOrgStructureTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
