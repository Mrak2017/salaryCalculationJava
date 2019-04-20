import { NgModule } from '@angular/core';
import { PersonsJournalComponent } from './persons/persons-journal/persons-journal.component';
import { SharedModule } from "../shared/shared.module";
import { PersonsMainService } from "./persons/persons-main.service";
import { AddPersonDialogComponent } from './persons/add-person-dialog/add-person-dialog.component';
import { ReactiveFormsModule } from "@angular/forms";
import { ConfigurationsJournalComponent } from './configuration/configurations-journal/configurations-journal.component';
import { ConfigurationsMainService } from "./configuration/configurations-main.service";
import { EditConfigurationDialogComponent } from './configuration/edit-configuration-dialog/edit-configuration-dialog.component';
import { PersonPageComponent } from './persons/person-page/person-page.component';
import { PersonMainTabComponent } from './persons/person-page/tabs/person-main-tab/person-main-tab.component';
import { PersonGroupTabComponent } from './persons/person-page/tabs/person-group-tab/person-group-tab.component';
import { PersonOrgStructureTabComponent } from './persons/person-page/tabs/person-org-structure-tab/person-org-structure-tab.component';
import { EditPersonGroupDialogComponent } from './persons/edit-person-group-dialog/edit-person-group-dialog.component';
import { CalcPersonSalaryDialogComponent } from './persons/calc-person-salary-dialog/calc-person-salary-dialog.component';
import { CalcTotalSalaryDialogComponent } from './persons/calc-total-salary-dialog/calc-total-salary-dialog.component';

@NgModule({
  imports: [
    SharedModule,
    ReactiveFormsModule,
  ],
  declarations: [
    PersonsJournalComponent,
    AddPersonDialogComponent,
    ConfigurationsJournalComponent,
    EditConfigurationDialogComponent,
    PersonPageComponent,
    PersonMainTabComponent,
    PersonGroupTabComponent,
    PersonOrgStructureTabComponent,
    EditPersonGroupDialogComponent,
    CalcPersonSalaryDialogComponent,
    CalcTotalSalaryDialogComponent,
  ],
  exports: [
    PersonsJournalComponent,
    ConfigurationsJournalComponent,
  ],
  providers: [
    PersonsMainService,
    ConfigurationsMainService,
  ],
  entryComponents: [
    AddPersonDialogComponent,
    EditConfigurationDialogComponent,
    EditPersonGroupDialogComponent,
    CalcPersonSalaryDialogComponent,
    CalcTotalSalaryDialogComponent,
  ],
})
export class UserModule {
}
