import { Component, OnDestroy, OnInit } from "@angular/core";
import { EntityService } from "@codegen/rescueapp/api/entity.service";
import { AssistanceDto, EntityDto } from "@codegen/rescueapp/model/models";
import { JwtService } from "../../commons/services/jwt.service";
import { AssistanceService } from "@codegen/rescueapp";
import * as moment from "moment";
import { ChatService } from "../../commons/services/chat.service";
import { AssistanceInterface, ChatComponent } from "../chat/chat.component";
import { MatDialog } from "@angular/material/dialog";


@Component({
  selector: "app-home",
  templateUrl: "./home.component.html",
  styleUrls: ["./home.component.scss"]
})
export class HomeComponent implements OnInit, OnDestroy {
  availableEntityDtoList: EntityDto[] = [];
  unavailableEntityDtoList: EntityDto[] = [];
  entityListInterval!: ReturnType<typeof setTimeout>;
  assistanceList: AssistanceDto[] = [];

  displayedColumns: string[] = ["id", "entity", "timestamp", "status"];

  constructor(
    private entityService: EntityService,
    private jwtService: JwtService,
    private assistanceService: AssistanceService,
    private chatService: ChatService,
    private matDialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.getEntityList();
    this.getAssistanceHistory();
    this.entityListInterval = setInterval(() => {
      this.getEntityList();
      this.getAssistanceHistory();
    }, 2000);
  }

  ngOnDestroy(): void {
    clearInterval(this.entityListInterval);
  }

  getEntityList(): void {
    this.entityService.getEntityAvailability().subscribe((entityDtoList: EntityDto[]) => {
        this.availableEntityDtoList = entityDtoList.filter(entityDto => entityDto.available);
        this.unavailableEntityDtoList = entityDtoList.filter(entityDto => !entityDto.available);
      }, (error) => {
        console.log("Error: ", error);
      }
    );
  }

  getIfAssistanceInProgressForEntity(entityDto: EntityDto): boolean {
    return this.chatService.hasEntityInProgressAssistance(entityDto.id!);
  }

  getAssistanceHistory(): void {
    this.assistanceService.getAssistanceListByUserId(
      this.jwtService.getUserIdFromToken()
    ).subscribe((assistanceDtoList: AssistanceDto[]) => {
      this.assistanceList = assistanceDtoList;
    }, (error) => {
      console.log("Error: ", error);
    });
  }

  getTimeFromDBTimestamp(ts: string): string {
    return moment(ts).format("HH:mm DD/MM/YYYY");
  }

  openChat(entityDto: EntityDto): void {
    this.matDialog.open(ChatComponent, {
      data: {
        entityId: entityDto.id!,
        entityName: entityDto.name
      } as AssistanceInterface,
      width: "100%"
    });
  }
}
