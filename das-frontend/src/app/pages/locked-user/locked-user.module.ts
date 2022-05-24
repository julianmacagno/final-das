import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { LockedUserRoutingModule } from "./locked-user-routing.module";
import { LockedUserComponent } from "./locked-user.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { TranslateModule } from "@ngx-translate/core";


@NgModule({
  declarations: [
    LockedUserComponent
  ],
  imports: [
    CommonModule,
    LockedUserRoutingModule,
    MatCardModule,
    MatButtonModule,
    TranslateModule
  ]
})
export class LockedUserModule {
}
