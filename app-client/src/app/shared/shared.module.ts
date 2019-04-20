import { NgModule } from '@angular/core';
import {
  MatButtonModule,
  MatCardModule,
  MatDatepickerModule,
  MatDialogModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatNativeDateModule,
  MatSelectModule,
  MatSidenavModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTreeModule,
} from "@angular/material";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { SimpleYesNoDialogComponent } from './common-dialogs/simple-yes-no-dialog/simple-yes-no-dialog.component';
import { RouterModule } from "@angular/router";

@NgModule({
  imports: [
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule,
    MatDialogModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    RouterModule,
    MatTabsModule,
    MatCardModule,
    MatTreeModule,
  ],
  exports: [
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatTableModule,
    MatIconModule,
    MatDialogModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    SimpleYesNoDialogComponent,
    RouterModule,
    MatTabsModule,
    MatCardModule,
    MatTreeModule,
  ],
  declarations: [
    SimpleYesNoDialogComponent,
  ],
  entryComponents: [
    SimpleYesNoDialogComponent,
  ],
})
export class SharedModule {
}
