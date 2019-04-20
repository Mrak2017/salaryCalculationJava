import { Inject, Injectable } from '@angular/core';
import { merge, Observable, Subject } from "rxjs/index";
import { HttpClient } from "@angular/common/http";
import { filter, map, shareReplay, switchMap, take } from "rxjs/internal/operators";
import { CheckUtils } from "../../utils/check-utils";
import { MatDialog } from "@angular/material";
import { ConfigurationItem } from "./models/configuration-item.model";
import { Configuration } from "./models/configuration.model";
import { EditConfigurationDialogComponent } from "./edit-configuration-dialog/edit-configuration-dialog.component";
import { SimpleYesNoDialogComponent } from "../../shared/common-dialogs/simple-yes-no-dialog/simple-yes-no-dialog.component";

@Injectable()
export class ConfigurationsMainService {

  public readonly allConfigs$: Observable<ConfigurationItem[]>;

  private readonly refreshSubj: Subject<void> = new Subject<void>();

  constructor(private http: HttpClient,
              @Inject('BASE_URL') private baseUrl: string,
              private dialog: MatDialog) {

    const refreshed$ = this.refreshSubj.asObservable().pipe(
        switchMap(() => this.getAllConfigs()),
    );

    const initial$ = this.getAllConfigs();

    this.allConfigs$ = merge(initial$, refreshed$).pipe(shareReplay(1));
  }

  refresh() {
    this.refreshSubj.next();
  }

  addConfig() {
    this.editDialog(undefined, 'Добавить настройку', dto => this.http.post(this.restUrl() + 'AddConfig', dto));
  }

  editConfig(id: number) {
    this.http.get<Configuration>(this.restUrl() + 'GetConfig/' + id)
        .pipe(map(value => new Configuration(value)))
        .toPromise()
        .then(conf => this.editDialog(conf, 'Изменить настройку', dto => this.http.put(this.restUrl() + 'UpdateConfig', dto)));
  }

  deleteConfig(id: number, code: string) {
    const dialogRef = this.dialog.open(SimpleYesNoDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        message: 'Вы уверены, что хотите удалить настройку с кодом "' + code + '"?',
      },
    });

    dialogRef.afterClosed()
        .pipe(
            filter(CheckUtils.isExists),
            switchMap(() => this.deleteConfigInternal(id)),
            take(1))
        .toPromise()
        .then(() => this.refresh());
  }

  private editDialog(conf: Configuration = new Configuration(), title: string, callback: Function) {
    const dialogRef = this.dialog.open(EditConfigurationDialogComponent, {
      width: '400px',
      disableClose: true,
      data: {
        configuration: conf,
        title: title,
      },
    });

    dialogRef.afterClosed()
        .pipe(
            filter(CheckUtils.isExists),
            map((result: Configuration) => (
                {
                  id: result.id,
                  code: result.code,
                  value: result.configValue,
                  description: result.description,
                })),
            switchMap(dto => callback(dto)),
            take(1))
        .toPromise()
        .then(() => this.refresh());
  }

  private getAllConfigs(): Observable<ConfigurationItem[]> {
    return this.http.get<ConfigurationItem[]>(this.restUrl() + 'AllConfigs')
        .pipe(
            map(data => data.map(value => new ConfigurationItem(value))),
        )
  }

  private deleteConfigInternal(id: number): Observable<void> {
    return this.http.delete<void>(this.restUrl() + 'DeleteConfig/' + id);
  }

  private restUrl(): string {
    return this.baseUrl + 'api/configuration/';
  }
}
