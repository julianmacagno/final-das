import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ChatManagerComponent } from "./chat-manager.component";
import { MatIconModule } from "@angular/material/icon";
import { MatButtonModule } from "@angular/material/button";
import { MatDialogModule } from "@angular/material/dialog";
import { ChatModule } from "../../../pages/chat/chat.module";
import { MatTooltipModule } from "@angular/material/tooltip";
import { TranslateModule } from "@ngx-translate/core";

@NgModule({
  declarations: [
    ChatManagerComponent
  ],
  exports: [
    ChatManagerComponent
  ],
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    ChatModule,
    MatTooltipModule,
    TranslateModule
  ]
})
export class ChatManagerModule {
}
