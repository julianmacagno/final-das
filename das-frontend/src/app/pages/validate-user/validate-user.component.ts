import { Component, OnInit } from "@angular/core";
import { AuthenticationService } from "@codegen/rescueapp/api/authentication.service";
import { ActivatedRoute, Router } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { TranslateService } from "@ngx-translate/core";
import { NotificationService } from "../../commons/services/notification.service";

@Component({
  selector: "app-validate-user",
  templateUrl: "./validate-user.component.html",
  styleUrls: ["./validate-user.component.scss"]
})
export class ValidateUserComponent implements OnInit {
  constructor(
    private authenticationService: AuthenticationService,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private router: Router,
    private notificationService: NotificationService
  ) {
  }

  ngOnInit(): void {
    const cuil = this.activatedRoute.snapshot.paramMap.get("cuil")!;
    this.authenticationService.validateUser(cuil).subscribe(() => {
      this.notificationService.showSuccess("VALIDATE_USER.NOTIFICATION.SUCCESS");
    });
  }

  goToLogin(): void {
    this.router.navigate([AppRoutes.login]).then();
  }
}
