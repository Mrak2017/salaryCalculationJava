import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditConfigurationDialogComponent } from './edit-configuration-dialog.component';

describe('EditConfigurationDialogComponent', () => {
  let component: EditConfigurationDialogComponent;
  let fixture: ComponentFixture<EditConfigurationDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditConfigurationDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditConfigurationDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
