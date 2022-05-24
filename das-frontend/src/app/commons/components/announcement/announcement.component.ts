import { Component, OnInit } from "@angular/core";
import { AnnouncementService } from "@codegen/rescueapp/api/announcement.service";
import { AnnouncementDto } from "@codegen/rescueapp/model/models";

@Component({
  selector: "app-announcement",
  templateUrl: "./announcement.component.html",
  styleUrls: ["./announcement.component.scss"]
})
export class AnnouncementComponent implements OnInit {
  announcementList!: string[];

  constructor(
    private announcementService: AnnouncementService
  ) {
  }

  ngOnInit(): void {
    this.getAnnouncements();
  }

  private getAnnouncements(): void {
    this.announcementService.getAnnouncement().subscribe((announcementDtoList: AnnouncementDto[]) => {
      this.announcementList = announcementDtoList.map(announcementDto => announcementDto.content!);
    }, error => {
      console.log("Error: ", error);
    });
  }
}
