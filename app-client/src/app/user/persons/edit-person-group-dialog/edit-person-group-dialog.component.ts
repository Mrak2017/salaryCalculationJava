import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { EditPersonGroupDialogData } from "../models/edit-person-group-dialog-data";
import { GroupTypeEnum } from "../models/group-type-enum";
import { Subscriber } from "../../../shared/subscriber";
import { CheckUtils } from "../../../utils/check-utils";

@Component({
  selector: 'app-edit-person-group-dialog',
  templateUrl: './edit-person-group-dialog.component.html',
  styleUrls: ['./edit-person-group-dialog.component.css'],
})
export class EditPersonGroupDialogComponent extends Subscriber implements OnInit {

  groupForm: FormGroup;
  readonly groupTypes = GroupTypeEnum.values();

  error: any = { isError: false, errorMessage: '' };

  constructor(public dialogRef: MatDialogRef<EditPersonGroupDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: EditPersonGroupDialogData,
              private fb: FormBuilder) {
    super();
  }

  ngOnInit() {
    this.groupForm = this.fb.group({
      groupType: [this.data.group.group, [Validators.required]],
      periodStart: [this.data.group.periodStart, [Validators.required]],
      periodEnd: [this.data.group.periodEnd, []],
    });

    const formValueChangesSubscription = this.groupForm.valueChanges.subscribe(() => this.compareTwoDates());
    this.subscribed(formValueChangesSubscription);
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit() {
    this.dialogRef.close({
      id: this.data.group.id,
      group: this.groupForm.value.groupType,
      periodStart: this.groupForm.value.periodStart,
      periodEnd: this.groupForm.value.periodEnd,
    });
  }

  private compareTwoDates() {
    if (CheckUtils.isExists(this.groupForm.value.periodEnd)
        && CheckUtils.isExists(this.groupForm.value.periodStart)
        && new Date(this.groupForm.value.periodEnd) < new Date(this.groupForm.value.periodStart)) {
      this.error = {
        isError: true, errorMessage: 'Дата начала периода не может быть позже даты завершения.'}
      }
    }

  }
