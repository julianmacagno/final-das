import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { HomeRoutingModule } from "./home-routing.module";
import { HomeComponent } from "./home.component";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { TranslateModule } from "@ngx-translate/core";
import { MatButtonModule } from "@angular/material/button";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatTableModule } from "@angular/material/table";
import { AnnouncementModule } from "../../commons/components/announcement/announcement.module";
import { MatIconModule } from "@angular/material/icon";


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatCardModule,
    MatFormFieldModule,
    TranslateModule,
    MatButtonModule,
    MatGridListModule,
    MatTableModule,
    AnnouncementModule,
    MatIconModule
  ]
})
export class HomeModule {
}
