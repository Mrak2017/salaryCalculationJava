import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { CalcPersonSalaryDialogData } from "../models/calc-person-salary-dialog-data";
import { Person } from "../models/person.model";
import { Subscriber } from "../../../shared/subscriber";
import { FormControl } from "@angular/forms";

@Component({
  selector: 'app-calc-person-salary-dialog',
  templateUrl: './calc-person-salary-dialog.component.html',
  styleUrls: ['./calc-person-salary-dialog.component.css'],
})
export class CalcPersonSalaryDialogComponent extends Subscriber implements OnInit {

  person: Person;

  calcDate = new FormControl(new Date());
  result: number;

  constructor(public dialogRef: MatDialogRef<CalcPersonSalaryDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: CalcPersonSalaryDialogData) {
    super();
  }

  ngOnInit() {
    this.person = this.data.person;
  }

  calculate() {
    this.result = null;
    const subscription = this.data.calcSalaryFunction.call(null, this.person.id, this.calcDate.value)
        .subscribe(val => this.result = val);
    this.subscribed(subscription);
  }

  onCancelClick() {
    this.dialogRef.close();
  }
}
