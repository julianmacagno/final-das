import { Injectable } from "@angular/core";
import { AssistanceDto, AssistanceDtoAssistanceStatus, MessageDto, StatusDto } from "@codegen/rescueapp/model/models";
import { AppRoutes } from "../enums/AppRoutes";
import { StatusService } from "@codegen/rescueapp/api/status.service";
import { JwtService } from "./jwt.service";
import { BehaviorSubject, Observable } from "rxjs";
import { MessageService } from "@codegen/rescueapp/api/message.service";
import { Router } from "@angular/router";

@Injectable({
  providedIn: "root"
})
export class ChatService {
  private userId!: number;
  map: Map<number, BehaviorSubject<ChatInterface>> = new Map<number, BehaviorSubject<ChatInterface>>();
  private statusUserInterval!: ReturnType<typeof setTimeout>;

  constructor(
    private statusService: StatusService,
    private jwtService: JwtService,
    private messageService: MessageService,
    private router: Router
  ) {
    this.init();
  }

  private init(): void {
    this.map.set(1, new BehaviorSubject<ChatInterface>({}));
    this.map.set(2, new BehaviorSubject<ChatInterface>({}));
    this.map.set(3, new BehaviorSubject<ChatInterface>({}));
    this.map.set(4, new BehaviorSubject<ChatInterface>({}));
    this.map.set(5, new BehaviorSubject<ChatInterface>({}));

    this.userId = this.jwtService.getUserIdFromToken();
    this.getStatusForUser();
    this.getNewMessages();
    this.statusUserInterval = setInterval(() => {
      if (this.jwtService.isTokenValid()) {
        this.getStatusForUser();
        this.getNewMessages();
      } else {
        clearInterval(this.statusUserInterval);
      }
    }, 2000);
  }

  private getNewMessages(): void {
    this.map.forEach((value: BehaviorSubject<ChatInterface>, _: number) => {
      const chatInterface: ChatInterface = value.getValue();
      if (chatInterface.currentAssistance?.assistanceStatus === AssistanceDtoAssistanceStatus.INPROGRESS) {
        this.messageService.getMessage(chatInterface.currentAssistance!.id!).subscribe((messageDtoList: MessageDto[]) => {
          chatInterface.messageDtoList = messageDtoList;
          value.next(chatInterface);
        }, error => {
          console.log("Error: ", error);
        });
      }
    });
  }

  public getStatusForEntity(entityId: number): Observable<ChatInterface> {
    return this.map.get(entityId)?.asObservable()!;
  }

  public hasEntityInProgressAssistance(entityId: number): boolean {
    return this.map.get(entityId)?.getValue().currentAssistance?.assistanceStatus === AssistanceDtoAssistanceStatus.INPROGRESS;
  }

  private getStatusForUser(): void {
    this.statusService.getStatusForUser(this.userId).subscribe((statusDto: StatusDto) => {
      if (statusDto.userCanceled) {
        clearInterval(this.statusUserInterval);
        this.jwtService.destroySession();
        this.router.navigate([AppRoutes.login]).then();
      } else {
        statusDto.currentAssistanceList!.forEach((assistanceDto: AssistanceDto) => {
          this.map.get(assistanceDto.entityId!)!.next(this.getChatInterface(statusDto, assistanceDto));
        });
      }
    });
  }

  private getChatInterface(statusDto: StatusDto, assistanceDto: AssistanceDto): ChatInterface {
    return {
      userId: statusDto.userId,
      userCanceled: statusDto.userCanceled,
      currentAssistance: assistanceDto
    } as ChatInterface;
  }
}

export interface ChatInterface {
  messageDtoList?: MessageDto[];
  userId?: number;
  userCanceled?: boolean;
  currentAssistance?: AssistanceDto;
}

export interface ValorationInterface {
  value?: number;
  positive: boolean;
}
