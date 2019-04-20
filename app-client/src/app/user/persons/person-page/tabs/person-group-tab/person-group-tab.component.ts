import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { Observable } from "rxjs/index";
import { PersonGroup } from "../../../models/person-group.model";
import { PersonPageService } from "../../person-page.service";
import { map } from "rxjs/internal/operators";

@Component({
  selector: 'app-person-group-tab',
  templateUrl: './person-group-tab.component.html',
  styleUrls: ['./person-group-tab.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonGroupTabComponent implements OnInit {

  displayedColumns: string[] = [
    'id',
    'groupType',
    'periodStart',
    'periodEnd',
    'editColumn',
    'deleteColumn'];

  groups$: Observable<PersonGroup[]>;

  constructor(private service: PersonPageService) {
  }

  ngOnInit() {
    this.groups$ = this.service.person$.pipe(map(p => p.groups));
  }

  addGroup() {
    this.service.addGroup();
  }

  editGroup(id: number) {
    this.service.editGroup(id);
  }

  deleteGroup(group: PersonGroup) {
    this.service.deleteGroup(group);
  }
}
