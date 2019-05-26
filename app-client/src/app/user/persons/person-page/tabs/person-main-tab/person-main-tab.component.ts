import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { PersonPageService } from "../../person-page.service";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { map, take } from "rxjs/internal/operators";
import { Person } from "../../../models/person.model";
import { Subscriber } from "../../../../../shared/subscriber";

@Component({
  selector: 'app-person-main-tab',
  templateUrl: './person-main-tab.component.html',
  styleUrls: ['./person-main-tab.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PersonMainTabComponent extends Subscriber implements OnInit {

  personForm: FormGroup;
  readonly nameMaxLength = Person.NAME_MAX_LENGTH;

  constructor(private service: PersonPageService,
              private fb: FormBuilder) {
    super();
  }

  ngOnInit() {
    this.personForm = this.fb.group({
      lastName: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      firstName: ['', [
        Validators.required,
        Validators.maxLength(Person.NAME_MAX_LENGTH)]],
      startDate: ['', [Validators.required]],
      endDate: [''],
      baseSalaryPart: ['', []],
    });

    const fillFormSubscription = this.service.person$.subscribe(person => this.fillForm(person));
    this.subscribed(fillFormSubscription);
  }

  onSubmit() {
    this.service.person$.pipe(
        map(p => this.fillOnSave(p)),
        take(1),
    )
        .toPromise()
        .then(p => this.service.updatePerson(p));
  }

  private fillForm(person: Person) {
    this.personForm.controls.lastName.setValue(person.lastName);
    this.personForm.controls.firstName.setValue(person.firstName);
    this.personForm.controls.startDate.setValue(person.startDate);
    this.personForm.controls.endDate.setValue(person.endDate);
    this.personForm.controls.baseSalaryPart.setValue(person.baseSalaryPart);
  }

  private fillOnSave(person: Person): Person {
    let clone = Object.assign({}, person);
    clone.lastName = this.personForm.value.lastName;
    clone.firstName = this.personForm.value.firstName;
    clone.startDate = this.personForm.value.startDate;
    clone.endDate = this.personForm.value.endDate;
    clone.baseSalaryPart = this.personForm.value.baseSalaryPart;
    return clone;
  }

}
