import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PersonsJournalComponent } from "./user/persons/persons-journal/persons-journal.component";
import { ConfigurationsJournalComponent } from "./user/configuration/configurations-journal/configurations-journal.component";
import { PersonPageComponent } from "./user/persons/person-page/person-page.component";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'persons',
  }, {
    path: 'persons',
    children: [{
      path: '',
      pathMatch: 'full',
      component: PersonsJournalComponent,
    },{
      path: ':id',
      component: PersonPageComponent,
    }]
  }, {
    path: 'configurations',
    component: ConfigurationsJournalComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
