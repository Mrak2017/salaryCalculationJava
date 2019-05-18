import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs";
import { PersonsMainService } from "../persons-main.service";
import { PersonItem } from "../models/person-item.model";

@Component({
  selector: 'app-persons-journal',
  templateUrl: './persons-journal.component.html',
  styleUrls: ['./persons-journal.component.css'],
})
export class PersonsJournalComponent implements OnInit {

  displayedColumns: string[] = [
    'id',
    'fullName',
    'startDate',
    'currentGroup',
    'baseSalaryPart',
    'currentSalary',
    'editColumn',
    'calcSalaryColumn',
  ];

  persons$: Observable<PersonItem[]>;

  constructor(private service: PersonsMainService) {
  }

  ngOnInit() {
    this.persons$ = this.service.allPersons$;
    this.service.refresh();
  }

  addPerson() {
    this.service.addPerson();
  }

  applyFilter(search: string) {
    this.service.refresh(search);
  }

  calcSalary(id: number) {
    this.service.calcSalaryDialog(id);
  }

  calcTotalSalary() {
    this.service.calcTotalSalaryDialog();
  }
}
