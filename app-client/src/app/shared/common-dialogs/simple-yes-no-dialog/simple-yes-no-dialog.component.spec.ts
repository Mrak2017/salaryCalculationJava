import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleYesNoDialogComponent } from './simple-yes-no-dialog.component';

describe('SimpleYesNoDialogComponent', () => {
  let component: SimpleYesNoDialogComponent;
  let fixture: ComponentFixture<SimpleYesNoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SimpleYesNoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleYesNoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
