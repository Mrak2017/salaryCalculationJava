import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CalcPersonSalaryDialogComponent } from './calc-person-salary-dialog.component';

describe('CalcPersonSalaryDialogComponent', () => {
  let component: CalcPersonSalaryDialogComponent;
  let fixture: ComponentFixture<CalcPersonSalaryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalcPersonSalaryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CalcPersonSalaryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
