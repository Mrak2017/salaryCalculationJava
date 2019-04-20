import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CalcTotalSalaryDialogComponent } from './calc-total-salary-dialog.component';

describe('CalcTotalSalaryDialogComponent', () => {
  let component: CalcTotalSalaryDialogComponent;
  let fixture: ComponentFixture<CalcTotalSalaryDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CalcTotalSalaryDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CalcTotalSalaryDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
