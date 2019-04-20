import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs/index";
import { ConfigurationsMainService } from "../configurations-main.service";
import { ConfigurationItem } from "../models/configuration-item.model";

@Component({
  selector: 'app-configurations-journal',
  templateUrl: './configurations-journal.component.html',
  styleUrls: ['./configurations-journal.component.css'],
})
export class ConfigurationsJournalComponent implements OnInit {

  displayedColumns: string[] = [
    'id',
    'insertDate',
    'updateDate',
    'code',
    'value',
    'description',
    'editColumn',
    'deleteColumn'];

  configs$: Observable<ConfigurationItem[]>;

  constructor(private service: ConfigurationsMainService) {
  }

  ngOnInit() {
    this.configs$ = this.service.allConfigs$;
  }

  addConfig() {
    this.service.addConfig();
  }

  editConfig(id: number) {
    this.service.editConfig(id);
  }

  deleteConfig(id: number, code: string) {
    this.service.deleteConfig(id, code);
  }

}
