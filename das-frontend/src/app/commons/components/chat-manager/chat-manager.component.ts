import { Component, OnInit } from "@angular/core";
import { StatusService } from "@codegen/rescueapp/api/status.service";
import { JwtService } from "../../services/jwt.service";
import { AssistanceDto, AssistanceDtoAssistanceStatus } from "@codegen/rescueapp/model/models";
import { Router } from "@angular/router";
import { ChatInterface, ChatService } from "../../services/chat.service";
import { AssistanceInterface, ChatComponent } from "../../../pages/chat/chat.component";
import { MatDialog } from "@angular/material/dialog";
import { BehaviorSubject } from "rxjs";

@Component({
  selector: "app-chat-manager",
  templateUrl: "./chat-manager.component.html",
  styleUrls: ["./chat-manager.component.scss"]
})
export class ChatManagerComponent implements OnInit {
  userId!: number;
  map: Map<number, ChatInterface> = new Map<number, ChatInterface>();

  constructor(
    private statusService: StatusService,
    private jwtService: JwtService,
    private router: Router,
    private chatService: ChatService,
    private matDialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.userId = this.jwtService.getUserIdFromToken();
    this.getStatusForUser();
  }

  private getStatusForUser(): void {
    this.chatService.map.forEach((value: BehaviorSubject<ChatInterface>, key: number) => {
      value.subscribe((chatInterface: ChatInterface) => {
        this.map.set(key, chatInterface);
      });
    });
  }

  getMapValues(): ChatInterface[] {
    return Array.from(this.map.values())
      .filter((chatInterface: ChatInterface) => chatInterface.currentAssistance?.assistanceStatus === AssistanceDtoAssistanceStatus.INPROGRESS);
  }

  getFirstLetter(assistanceDto: AssistanceDto): string {
    return assistanceDto ? assistanceDto.entityName!.charAt(0) : "";
  }

  openChat(assistanceDto: AssistanceDto): void {
    this.matDialog.open(ChatComponent, {
      data: {
        entityId: assistanceDto.entityId!
      } as AssistanceInterface,
      width: "100%"
    });
  }
}
