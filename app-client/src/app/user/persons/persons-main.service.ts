import { Inject, Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { merge, Observable, Subject } from "rxjs/index";
import { debounceTime, filter, map, shareReplay, switchMap, take } from "rxjs/internal/operators";
import { MatDialog } from "@angular/material";
import { AddPersonDialogComponent } from "./add-person-dialog/add-person-dialog.component";
import { CheckUtils } from "../../utils/check-utils";
import { PersonItem } from "./models/person-item.model";
import { Person } from "./models/person.model";
import { ComboBoxItemDTO } from "../../shared/models/combobox-item-dto";
import { DateUtils } from "../../utils/date-utils";
import { PersonGroup } from "./models/person-group.model";
import { EditPersonGroupDialogComponent } from "./edit-person-group-dialog/edit-person-group-dialog.component";
import { SimpleYesNoDialogComponent } from "../../shared/common-dialogs/simple-yes-no-dialog/simple-yes-no-dialog.component";
import { CalcPersonSalaryDialogComponent } from "./calc-person-salary-dialog/calc-person-salary-dialog.component";
import { CalcTotalSalaryDialogComponent } from "./calc-total-salary-dialog/calc-total-salary-dialog.component";

@Injectable()
export class PersonsMainService {

  public readonly allPersons$: Observable<PersonItem[]>;

  private readonly refreshSubj: Subject<string> = new Subject<string>();

  constructor(private http: HttpClient,
              @Inject('BASE_URL') private baseUrl: string,
              private dialog: MatDialog) {
    const refreshed$ = this.refreshSubj.asObservable().pipe(
        debounceTime(300),
        switchMap(search => this.getAllPersons(search)),
    );

    const initial$ = this.getAllPersons();

    this.allPersons$ = merge(initial$, refreshed$).pipe(shareReplay(1));
  }

  refresh(search: string = "") {
    this.refreshSubj.next(search);
  }

  addPerson() {
    const dialogRef = this.dialog.open(AddPersonDialogComponent, {
      width: '400px',
      disableClose: true,
    });

    dialogRef.afterClosed()
        .pipe(
            filter(CheckUtils.isExists),
            map(PersonsMainService.convertToDTO),
            switchMap(dto => this.http.post(this.restUrl(), dto)),
            take(1))
        .toPromise()
        .then(() => this.refresh());
  }

  getPerson(id: number): Observable<Person> {
    return this.http.get<Person>(this.restUrl() + id)
        .pipe(
            map(data => new Person(data)),
        );
  }

  updatePerson(person: Person): Observable<{}> {
    return this.http.put(this.restUrl() + 'UpdatePerson/' + person.id, PersonsMainService.convertToDTO(person));
  }

  getPossibleChiefs(): Observable<ComboBoxItemDTO[]> {
    return this.http.get<ComboBoxItemDTO[]>(this.restUrl() + 'GetPossibleChiefs')
        .pipe(
            map(data => data.map(value => new ComboBoxItemDTO(value.id, value.name))),
        )
  }

  updateChief(personId: number, newChiefId: number): Observable<{}> {
    return this.http.put(this.restUrl() + 'UpdateChief/' + personId + "/" + newChiefId, {});
  }

  addGroup(personId: number): Promise<{}> {
    return this.editGroupDialog(undefined, 'Добавить группу',
        dto => this.http.post(this.restUrl() + personId + '/AddGroup', dto));
  }

  updateGroup(id: number): Promise<{}> {
    return this.http.get<PersonGroup>(this.restUrl() + 'GetGroup/' + id)
        .pipe(map(value => new PersonGroup(value)))
        .toPromise()
        .then(group => this.editGroupDialog(group, 'Изменить группу',
            dto => this.http.put(this.restUrl() + 'UpdateGroup', dto)));
  }

  deleteGroup(group: PersonGroup): Promise<void> {
    const periodEndPart = CheckUtils.isExists(group.periodEnd) ? '" по "' + DateUtils.formatDateOnly(group.periodEnd) : '';
    const message = 'Вы уверены, что хотите удалить группу "' + group.group.name
        + '" за период с "' + DateUtils.formatDateOnly(group.periodStart)
        + periodEndPart + '" ?';

    const dialogRef = this.dialog.open(SimpleYesNoDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        message: message,
      },
    });

    return dialogRef.afterClosed()
        .pipe(
            filter(CheckUtils.isExists),
            switchMap(() => this.deleteGroupInternal(group.id)),
            take(1))
        .toPromise();
  }

  getPossibleSubordinates(id: number): Observable<ComboBoxItemDTO[]> {
    return this.http.get<ComboBoxItemDTO[]>(this.restUrl() + id + '/GetPossibleSubordinates')
        .pipe(
            map(data => data.map(value => new ComboBoxItemDTO(value.id, value.name))),
        )
  }

  calcSalaryDialog(id: number) {
    this.getPerson(id)
        .pipe(take(1))
        .toPromise()
        .then(person => this.calcSalaryDialogInternal(person));
  }

  calcTotalSalaryDialog() {
    const dialogRef = this.dialog.open(CalcTotalSalaryDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        calcSalaryFunction: (calcDate: Date) => this.calcTotalSalary(calcDate),
      },
    });

    dialogRef.afterClosed()
        .toPromise()
        .then(() => this.refresh());
  }

  private calcSalaryDialogInternal(person: Person) {
    const dialogRef = this.dialog.open(CalcPersonSalaryDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        person: person,
        calcSalaryFunction: (id: number, calcDate: Date) => this.calcSalary(id, calcDate),
      },
    });

    dialogRef.afterClosed()
        .toPromise()
        .then(() => this.refresh());
  }

  private getAllPersons(search: string = null): Observable<PersonItem[]> {
    const param = CheckUtils.isExists(search) ? "?q=" + search : "";
    return this.http.get<PersonItem[]>(this.restUrl() + 'journal' + param)
        .pipe(
            map(data => data.map(value => new PersonItem(value))),
        )
  }

  private editGroupDialog(group: PersonGroup = new PersonGroup(), title: string, callback: Function): Promise<{}> {
    const dialogRef = this.dialog.open(EditPersonGroupDialogComponent, {
      width: '500px',
      disableClose: true,
      data: {
        group: group,
        title: title,
      },
    });

    return dialogRef.afterClosed()
        .pipe(
            filter(CheckUtils.isExists),
            map((result: PersonGroup) => (
                {
                  id: result.id,
                  periodStart: DateUtils.formatNoTimeZoneDayStart(result.periodStart),
                  periodEnd: CheckUtils.isExists(result.periodEnd) ? DateUtils.formatNoTimeZoneDayEnd(result.periodEnd) : null,
                  groupType: CheckUtils.isExists(result.group) ? result.group.code : null,
                })),
            switchMap(dto => callback(dto)),
            take(1))
        .toPromise();
  }

  private deleteGroupInternal(id: number): Observable<void> {
    return this.http.delete<void>(this.restUrl() + 'DeleteGroup/' + id);
  }

  private calcSalary(id: number, calcDate: Date): Observable<number> {
    return this.http.get<number>(
        this.restUrl() + id + '/CalcSalaryOnDate?calcDate=' + DateUtils.formatNoTimeZoneDayStart(calcDate));
  }

  private calcTotalSalary(calcDate: Date): Observable<number> {
    return this.http.get<number>(
        this.restUrl() + 'CalcTotalSalaryOnDate?calcDate=' + DateUtils.formatNoTimeZoneDayStart(calcDate));
  }

  private static convertToDTO(person: Person) {
    return {
      id: person.id,
      firstName: person.firstName,
      lastName: person.lastName,
      startDate: DateUtils.formatNoTimeZoneDayStart(person.startDate),
      endDate: CheckUtils.isExists(person.endDate) ? DateUtils.formatNoTimeZoneDayEnd(person.endDate) : null,
      currentGroup: CheckUtils.isExists(person.currentGroup) ? person.currentGroup.code : null,
      baseSalaryPart: person.baseSalaryPart,
    };
  }

  private restUrl(): string {
    return this.baseUrl + 'api/persons/';
  }
}
