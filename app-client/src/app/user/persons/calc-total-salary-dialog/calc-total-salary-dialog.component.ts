import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { CalcTotalSalaryDialogData } from "../models/calc-total-salary-dialog-data";
import { Subscriber } from "../../../shared/subscriber";
import { FormControl } from "@angular/forms";

@Component({
  selector: 'app-calc-total-salary-dialog',
  templateUrl: './calc-total-salary-dialog.component.html',
  styleUrls: ['./calc-total-salary-dialog.component.css'],
})
export class CalcTotalSalaryDialogComponent extends Subscriber {

  calcDate = new FormControl(new Date());
  result: number;

  constructor(public dialogRef: MatDialogRef<CalcTotalSalaryDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: CalcTotalSalaryDialogData) {
    super();
  }

  calculate() {
    this.result = null;
    const subscription = this.data.calcSalaryFunction.call(null, this.calcDate.value)
        .subscribe(val => this.result = val);
    this.subscribed(subscription);
  }

  onCancelClick() {
    this.dialogRef.close();
  }
}
