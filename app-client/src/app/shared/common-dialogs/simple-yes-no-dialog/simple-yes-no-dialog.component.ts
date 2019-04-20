import { Component, Inject } from '@angular/core';
import { EditConfigurationDialogComponent } from "../../../user/configuration/edit-configuration-dialog/edit-configuration-dialog.component";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material";
import { SimpleYesNoDialogData } from "./simple-yes-no-dialog-data";

@Component({
  selector: 'app-simple-yes-no-dialog',
  templateUrl: './simple-yes-no-dialog.component.html',
  styleUrls: ['./simple-yes-no-dialog.component.css'],
})
export class SimpleYesNoDialogComponent {

  constructor(public dialogRef: MatDialogRef<EditConfigurationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: SimpleYesNoDialogData) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick() {
    this.dialogRef.close(true);
  }
}
