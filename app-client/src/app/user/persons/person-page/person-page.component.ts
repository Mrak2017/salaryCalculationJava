import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PersonPageService } from "./person-page.service";
import { Observable } from "rxjs";
import {map} from "rxjs/internal/operators";
import { Subscriber } from "../../../shared/subscriber";
import { CheckUtils } from "../../../utils/check-utils";

@Component({
  selector: 'app-person-page',
  templateUrl: './person-page.component.html',
  styleUrls: ['./person-page.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [PersonPageService],
})
export class PersonPageComponent extends Subscriber implements OnInit {

  login$: Observable<string>;
  currentGroup$: Observable<string>;
  currentSalary$: Observable<number>;
  fullName$: Observable<string>;
  currentChiefName$: Observable<string>;

  constructor(private service: PersonPageService) {
    super();
  }

  ngOnInit() {
    this.login$ = this.service.person$.pipe(map(p => p.login));
    this.currentGroup$ = this.service.person$.pipe(
        map(p => CheckUtils.isExists(p.currentGroup) ? p.currentGroup.name : null),
    );
    this.currentSalary$ = this.service.person$.pipe(map(p => p.currentSalary));
    this.fullName$ = this.service.person$.pipe(map(p => p.fullNameDots));
    this.currentChiefName$ = this.service.person$.pipe(
        map(p => CheckUtils.isExists(p.currentChief) ? p.currentChief.fullNameDots : "-")
    );
  }

}
