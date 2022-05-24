import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { ChatComponent } from "./chat.component";
import { MatCardModule } from "@angular/material/card";
import { MatInputModule } from "@angular/material/input";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { FormsModule } from "@angular/forms";
import { TranslateModule } from "@ngx-translate/core";
import { MatSelectModule } from "@angular/material/select";


@NgModule({
  declarations: [
    ChatComponent
  ],
  imports: [
    CommonModule,
    MatCardModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    FormsModule,
    TranslateModule,
    MatSelectModule
  ]
})
export class ChatModule {
}
