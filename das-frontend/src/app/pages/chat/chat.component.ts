import { AfterViewInit, Component, ElementRef, Inject, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { MessageService } from "@codegen/rescueapp/api/message.service";
import { NavigationStart, Router } from "@angular/router";
import { JwtService } from "../../commons/services/jwt.service";
import { AssistanceDtoAssistanceStatus, MessageDto, MessageDtoMessageType, ReasonDto } from "@codegen/rescueapp/model/models";
import { NotificationService } from "../../commons/services/notification.service";
import * as moment from "moment";
import { GeolocationService } from "../../commons/services/geolocation.service";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { ChatInterface, ChatService, ValorationInterface } from "../../commons/services/chat.service";
import { filter } from "rxjs/operators";
import { Subscription } from "rxjs";
import { ReasonService } from "@codegen/rescueapp/api/reason.service";
import { AppRoutes } from "src/app/commons/enums/AppRoutes";

@Component({
  selector: "app-chat",
  templateUrl: "./chat.component.html",
  styleUrls: ["./chat.component.scss"]
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewInit {
  messageDtoList: MessageDto[] = [];
  message: string | undefined;
  userId!: number;
  currentChatInterface!: ChatInterface;
  valorations: ValorationInterface[] =[];
  @ViewChild("messageBox") messageBox!: ElementRef;
  messageBoxObserver!: MutationObserver;
  firstTimeScroll = true;
  isAssistancePending = true;
  statusSubscription!: Subscription;
  reasonDtoList: ReasonDto[] = [];
  selectedReason = "";
  reasonFlag = false;

  constructor(
    private messageService: MessageService,
    private jwtService: JwtService,
    private router: Router,
    private notificationService: NotificationService,
    private geolocationService: GeolocationService,
    private chatService: ChatService,
    public matDialogRef: MatDialogRef<ChatComponent>,
    private reasonService: ReasonService,
    @Inject(MAT_DIALOG_DATA) public assistanceInterface: AssistanceInterface
  ) {
  }

  ngOnInit(): void {
    this.userId = this.jwtService.getUserIdFromToken();
    this.getCurrentAssistance();
    this.router.events.pipe(
      filter(event => event instanceof NavigationStart)
    ).subscribe(() => {
      this.matDialogRef.close();
    });
  }

  getReason(): void {
    this.reasonService.getReasons(this.assistanceInterface.entityId!).subscribe((reasonDtoList: ReasonDto[]) => {
      this.reasonDtoList = reasonDtoList;
      this.changeReasonFlag();
    });
  }

  private getCurrentAssistance(): void {
    this.statusSubscription = this.chatService.getStatusForEntity(this.assistanceInterface.entityId!).subscribe((chatInterface: ChatInterface) => {
      this.currentChatInterface = chatInterface;
      if (chatInterface.messageDtoList && chatInterface.messageDtoList.length > 0) {
        this.isAssistancePending = false;
        this.getNewMessages(chatInterface.messageDtoList!);
      }
    }, error => {
      console.log("Error: ", error);
    });
  }

  ngAfterViewInit(): void {
    this.messageBoxObserver = new MutationObserver(() => {
      if (this.firstTimeScroll) {
        this.messageBox.nativeElement.scrollTop = this.messageBox.nativeElement.scrollHeight;
        this.firstTimeScroll = false;
      } else if (this.messageBox.nativeElement.scrollTop + this.messageBox.nativeElement.clientHeight >= this.messageBox.nativeElement.scrollHeight - 100) {
        this.messageBox.nativeElement.scrollTop = this.messageBox.nativeElement.scrollHeight;
      }
    });
    this.messageBoxObserver.observe(this.messageBox.nativeElement, { childList: true });
  }

  ngOnDestroy(): void {
    this.messageBoxObserver.disconnect();
    this.statusSubscription.unsubscribe();
  }

  private getNewMessages(messageDtoList: MessageDto[]): void {
    if (this.currentChatInterface.currentAssistance?.id === undefined) {
      return;
    }

    if (messageDtoList.some(messageDto => messageDto.messageType === MessageDtoMessageType.FINALIZATION)) {
      let finalizationMessage = messageDtoList.find(messageDto => messageDto.messageType === MessageDtoMessageType.FINALIZATION);
      let valorations = JSON.parse(finalizationMessage!.payload!);
      this.valorations = valorations.valorations;
      this.notificationService.showSuccess("CHAT.NOTIFICATION.ASSISTANCE_FINALIZED");
    }

    const newMessages = messageDtoList.filter(messageDto =>
      [MessageDtoMessageType.MESSAGE, MessageDtoMessageType.CREATION].includes(messageDto.messageType!));
    if (newMessages.length !== this.messageDtoList.length) {
      this.messageDtoList = newMessages;
    }
  }

  private createAsistance(base64Attachment: string, fileType: string): Promise<MessageDto> {
    return new Promise((resolve, reject) => {
      this.geolocationService.getGeoLocation().then((geolocationCoordinates: GeolocationCoordinates) => {
        const messageDto: MessageDto = {
          entityId: this.assistanceInterface.entityId,
          userId: this.userId,
          messageType: MessageDtoMessageType.CREATION,
          isSynchronized: false,
          payload: JSON.stringify({
            lat: geolocationCoordinates.latitude,
            lng: geolocationCoordinates.longitude,
            message: this.message,
            attachmentType: fileType !== "" ? fileType : undefined
          }),
          attachment: base64Attachment !== "" ? base64Attachment : undefined,
          isFromUser: true,
          assistanceId: undefined
        };
        resolve(messageDto);
      }).catch(() => {
        this.notificationService.showError("CHAT.NOTIFICATION.LOCATION_NOT_PROVIDED");
        reject();
      });
    });
  }

  private createMessage(base64Attachment: string, fileType: string): MessageDto {
    return {
      messageType: MessageDtoMessageType.MESSAGE,
      isFromUser: true,
      entityId: this.assistanceInterface.entityId,
      userId: this.userId,
      assistanceId: this.currentChatInterface.currentAssistance?.id,
      payload: JSON.stringify(fileType ? { message: this.message, attachmentType: fileType ?? undefined } : { message: this.message }),
      isSynchronized: false,
      attachment: base64Attachment
    } as MessageDto;
  }

  sendValorationMessage(valoration: ValorationInterface): void {
    const messageDto: MessageDto = {};
    messageDto.messageType = MessageDtoMessageType.VALORATION;
    messageDto.isFromUser = true;
    messageDto.attachment = undefined;
    messageDto.payload = JSON.stringify({valoration: valoration})
    messageDto.entityId = this.assistanceInterface.entityId;
    messageDto.assistanceId = this.currentChatInterface.currentAssistance?.id;
    messageDto.isSynchronized = false;
    this.messageService.sendMessage(messageDto).subscribe(() => {
      this.router.navigate([AppRoutes.home]).then();
    });
  }

  sendMessageHelper(base64Attachment: string = "", fileType: string = ""): void {
    if (ChatComponent.isBlank(this.message) && base64Attachment === "") {
      return;
    }

    if (this.currentChatInterface.currentAssistance?.id === undefined) {
      this.createAsistance(base64Attachment, fileType).then((messageDto: MessageDto) => {
        this.messageService.sendMessage(messageDto).subscribe(() => {
          this.messageDtoList.push(messageDto);
          this.getCurrentAssistance();
          this.message = undefined;
        });
      }).catch(() => {
        this.notificationService.showError("SERVER_ERROR.ERROR_INSERTING_ASSISTANCE");
      });
    } else {
      const messageDto = this.createMessage(base64Attachment, fileType);
      this.messageService.sendMessage(messageDto).subscribe(() => {
        this.messageDtoList.push(messageDto);
        this.message = undefined;
      });
    }
  }

  cancelAssistance(selectedReason: string): void {
    const messageDto: MessageDto = {
      messageType: MessageDtoMessageType.CANCELATION,
      isFromUser: true,
      entityId: this.assistanceInterface.entityId,
      userId: this.userId,
      assistanceId: this.currentChatInterface.currentAssistance?.id,
      payload: JSON.stringify({
        message: this.message,
        attachmentType: null,
        cancelReason: selectedReason
      }),
      isSynchronized: false,
      attachment: undefined
    };
    this.messageService.sendMessage(messageDto).subscribe((result: boolean) => {
      if (result) {
        this.message = undefined;
        this.notificationService.showWarning("CHAT.NOTIFICATION.CANCELLATION_WARNING");
      }
    });
  }

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files![0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      this.sendMessageHelper(reader.result as string, file.type);
    };
  }

  canDisplayImageMsg(messageDto: MessageDto): boolean {
    return !ChatComponent.isBlank(messageDto.attachment) && this.getAttachmentTypeFromPayload(messageDto).includes("image");
  }

  canDisplayAudioMsg(messageDto: MessageDto): boolean {
    return !ChatComponent.isBlank(messageDto.attachment) && this.getAttachmentTypeFromPayload(messageDto!).includes("audio");
  }

  canDisplayTextMsg(messageDto: MessageDto): boolean {
    return !ChatComponent.isBlank(this.getMessageFromPayload(messageDto));
  }

  getMessageFromPayload(messageDto: MessageDto): string {
    return JSON.parse(messageDto.payload!).message ?? "";
  }

  getAttachmentTypeFromPayload(messageDto: MessageDto): string {
    return JSON.parse(messageDto.payload!).attachmentType ?? "";
  }

  getAssistantFromPayload(messageDto: MessageDto): string {
    return JSON.parse(messageDto.payload!).assistant ?? "";
  }

  getMessageTimestamp(messageDto: MessageDto): string {
    return moment(messageDto.timestamp).format("HH:mm DD/MM");
  }

  isChatDisabled(): boolean {
    if (this.isAssistancePending) {
      return false;
    } else if (this.currentChatInterface.currentAssistance?.assistanceStatus! === undefined) {
      return true;
    } else {
      return [AssistanceDtoAssistanceStatus.FINALIZED, AssistanceDtoAssistanceStatus.CANCELED]
        .includes(this.currentChatInterface.currentAssistance?.assistanceStatus!);
    }
  }

  private static isBlank(str: string | undefined): boolean {
    return str !== undefined && /^\s*$/.test(str);
  }

  changeReasonFlag(): void {
    this.reasonFlag = !this.reasonFlag;
  }
}

export interface AssistanceInterface {
  entityId: number;
  entityName: string;
}
