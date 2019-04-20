import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPersonDialogComponent } from './add-person-dialog.component';

describe('AddPersonDialogComponent', () => {
  let component: AddPersonDialogComponent;
  let fixture: ComponentFixture<AddPersonDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPersonDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPersonDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
