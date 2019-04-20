import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from "@angular/material";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { GroupTypeEnum } from "../models/group-type-enum";
import { Person } from "../models/person.model";

@Component({
  selector: 'app-add-person-dialog',
  templateUrl: './add-person-dialog.component.html',
  styleUrls: ['./add-person-dialog.component.css'],
})
export class AddPersonDialogComponent implements OnInit {

  personForm: FormGroup;
  readonly groupTypes = GroupTypeEnum.values();
  readonly nameMaxLength = Person.NAME_MAX_LENGTH;

  constructor(public dialogRef: MatDialogRef<AddPersonDialogComponent>,
              private fb: FormBuilder) {
  }

  ngOnInit() {
    this.personForm = this.fb.group({
      login: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      password: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      lastName: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      firstName: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      middleName: [''],
      startDate: ['', [Validators.required]],
      currentGroup: [GroupTypeEnum.EMPLOYEE, [Validators.required]],
      baseSalaryPart: ['', []],
    });
  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSubmit() {
    this.dialogRef.close(this.personForm.value);
  }

}
