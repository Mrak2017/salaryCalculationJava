import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPersonGroupDialogComponent } from './edit-person-group-dialog.component';

describe('EditPersonGroupDialogComponent', () => {
  let component: EditPersonGroupDialogComponent;
  let fixture: ComponentFixture<EditPersonGroupDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditPersonGroupDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditPersonGroupDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
