import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { EditConfigurationDialogData } from "../models/edit-configuration-dialog-data";

@Component({
  selector: 'app-edit-configuration-dialog',
  templateUrl: './edit-configuration-dialog.component.html',
  styleUrls: ['./edit-configuration-dialog.component.css'],
})
export class EditConfigurationDialogComponent implements OnInit {

  configForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<EditConfigurationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: EditConfigurationDialogData,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.configForm = this.fb.group({
      code: [this.data.configuration.code, [Validators.required]],
      configValue: [this.data.configuration.configValue, []],
      description: [this.data.configuration.description, []],
    });
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit() {
    this.dialogRef.close({
      id: this.data.configuration.id,
      code: this.configForm.value.code,
      configValue: this.configForm.value.configValue,
      description: this.configForm.value.description,
    });
  }

}
