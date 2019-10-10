import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { combineLatest, Observable } from "rxjs/index";
import { ComboBoxItemDTO } from "../../../../../shared/models/combobox-item-dto";
import { PersonPageService } from "../../person-page.service";
import { filter, map, shareReplay, withLatestFrom } from "rxjs/internal/operators";
import { CheckUtils } from "../../../../../utils/check-utils";
import { Subscriber } from "../../../../../shared/subscriber";
import { OrgStructureItem } from "../../../models/org-structure-item";
import { NestedTreeControl } from "@angular/cdk/tree";
import { MatTreeNestedDataSource } from "@angular/material";
import { GroupTypeEnum } from "../../../models/group-type-enum";

@Component({
  selector: 'app-person-org-structure-tab',
  templateUrl: './person-org-structure-tab.component.html',
  styleUrls: ['./person-org-structure-tab.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonOrgStructureTabComponent extends Subscriber implements OnInit {

  hasCurrentGroup$: Observable<boolean>;

  chiefs$: Observable<ComboBoxItemDTO[]>;
  selectedChief: ComboBoxItemDTO;

  personId$: Observable<number>;
  treeControl = new NestedTreeControl<OrgStructureItem>(node => node.children);
  dataSource = new MatTreeNestedDataSource<OrgStructureItem>();

  hasChild = (_: number, node: OrgStructureItem) => !!node.children && node.children.length > 0;

  currentGroupNotEmployee$: Observable<boolean>;
  subordinates$: Promise<ComboBoxItemDTO[]>;
  selectedSubordinate: ComboBoxItemDTO;

  constructor(private service: PersonPageService) {
    super();
  }

  ngOnInit() {
    const currentGroup$ = this.service.person$.pipe(
        filter(p => CheckUtils.isExists(p.currentGroup)),
        map(p => p.currentGroup.name),
    );
    this.hasCurrentGroup$ = currentGroup$.pipe(map(CheckUtils.isExists));
    this.personId$ = this.service.person$.pipe(map(p => p.id));

    this.fillChiefsData();
    this.fillSubordinatesData();

    const childrenSubscription = this.service.person$
        .pipe(map(p => p.children))
        .subscribe(children => this.dataSource.data = new Array(children));
    this.subscribed(childrenSubscription);
  }

  updateChief() {
    const newChiefId: number = CheckUtils.isExists(this.selectedChief) ? this.selectedChief.id : 0;
    this.service.updateChief(newChiefId);
  }

  addSubordinate() {
    if (CheckUtils.isExists(this.selectedSubordinate)) {
      this.service
          .addSubordinate(this.selectedSubordinate.id, this.selectedSubordinate.name)
          .then(() => this.selectedSubordinate = null);
    }
  }

  removeSubordinate(id: number, name: string) {
    this.service.removeSubordinate(id, name);
  }

  private fillChiefsData() {
    this.chiefs$ = this.service.getPossibleChiefs();
  }

  private fillSubordinatesData() {
    this.currentGroupNotEmployee$ = this.service.person$.pipe(
        map(p => p.currentGroup),
        filter(CheckUtils.isExists),
        map((val: GroupTypeEnum) => val !== GroupTypeEnum.EMPLOYEE));

    this.subordinates$ = this.service.getPossibleSubordinates();
  }

}
