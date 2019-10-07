import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { Person } from "../models/person.model";
import { merge, Subject } from "rxjs/index";
import { PersonsMainService } from "../persons-main.service";
import { filter, map, shareReplay, switchMap, take, withLatestFrom } from "rxjs/internal/operators";
import { ActivatedRoute } from "@angular/router";
import { ComboBoxItemDTO } from "../../../shared/models/combobox-item-dto";
import { PersonGroup } from "../models/person-group.model";
import { CheckUtils } from "../../../utils/check-utils";
import { SimpleYesNoDialogComponent } from "../../../shared/common-dialogs/simple-yes-no-dialog/simple-yes-no-dialog.component";
import { MatDialog } from "@angular/material";

@Injectable()
export class PersonPageService {

  public readonly person$: Observable<Person>;
  public readonly id$: Observable<number>;

  private readonly refreshSubj: Subject<void> = new Subject<void>();

  constructor(private service: PersonsMainService,
              private route: ActivatedRoute,
              private dialog: MatDialog) {
    const refreshed$ = this.refreshSubj.asObservable().pipe(
        withLatestFrom(this.route.params.pipe(map(p => p.id))),
        switchMap(([_, id]) => this.service.getPerson(id)),
    );

    const initial$ = this.route.params.pipe(
        map(p => p.id),
        switchMap(id => this.service.getPerson(id)),
    );

    this.person$ = merge(initial$, refreshed$)
        .pipe(shareReplay(1));

    this.id$ = this.person$.pipe(
        map(p => p.id),
        shareReplay(1));
  }

  updatePerson(person: Person) {
    this.service.updatePerson(person)
        .toPromise()
        .then(() => this.refresh());
  }

  getPossibleChiefs(): Observable<ComboBoxItemDTO[]> {
    return this.id$.pipe(
        switchMap(id => this.service.getPossibleChiefs(id)),
        shareReplay(1)
    );
  }

  updateChief(newChiefId: number) {
    this.person$.pipe(
        map(p => p.id),
        take(1),
    )
        .toPromise()
        .then((personId) => this.service.updateChief(personId, newChiefId).toPromise())
        .then(() => this.refresh());
  }

  addGroup() {
    this.person$.pipe(
        map(p => p.id),
        take(1),
    )
        .toPromise()
        .then((personId) => this.service.addGroup(personId))
        .then(() => this.refresh());
  }

  editGroup(id: number) {
    this.service.updateGroup(id).then(() => this.refresh());
  }

  deleteGroup(group: PersonGroup) {
    this.service.deleteGroup(group).then(() => this.refresh());
  }

  getPossibleSubordinates(): Promise<ComboBoxItemDTO[]> {
    return this.person$.pipe(
        map(p => p.id),
        take(1),
    )
        .toPromise()
        .then(id => this.service.getPossibleSubordinates(id).pipe(take(1)).toPromise());
  }

  addSubordinate(newSubordinateId: number, newSubordinateName: string): Promise<void> {
    return this.person$.pipe(
        take(1),
    )
        .toPromise()
        .then(person => this.addSubordinateInternal(newSubordinateId, newSubordinateName, person))
        .then(() => this.refresh());
  }

  removeSubordinate(id: number, name: string) {
    return this.person$.pipe(
        take(1),
    )
        .toPromise()
        .then(person => this.removeSubordinateInternal(id, name, person))
        .then(() => this.refresh());
  }

  private addSubordinateInternal(newSubordinateId: number, newSubordinateName: string, person: Person) {
    const message = "Вы уверены, что хотите изменить руководителя для '" + newSubordinateName
        + "' на '" + person.fullNameDots + "? Будет изменена вся ветка иерархии.";
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
            switchMap(() => this.service.updateChief(newSubordinateId, person.id).pipe(take(1)).toPromise()),
            take(1))
        .toPromise();
  }

  private removeSubordinateInternal(id: number, name: string, person: Person) {
    const message = "Вы уверены, что хотите исключить сотрудника '" + name
        + "' из списка подчиненных для '" + person.fullNameDots + "? Будет изменена вся ветка иерархии.";
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
            switchMap(() => this.service.updateChief(id, 0).pipe(take(1)).toPromise()),
            take(1))
        .toPromise();
  }

  private refresh() {
    this.refreshSubj.next();
  }

}
