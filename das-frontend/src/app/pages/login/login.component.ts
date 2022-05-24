import { Component } from "@angular/core";
import { FormControl, Validators } from "@angular/forms";
import { TranslateService } from "@ngx-translate/core";
import { JwtService } from "../../commons/services/jwt.service";
import { Router } from "@angular/router";
import { AuthenticationService } from "@codegen/rescueapp/api/authentication.service";
import { LoginDto } from "@codegen/rescueapp/model/loginDto";
import { AuthenticationDto } from "@codegen/rescueapp/model/authenticationDto";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { NotificationService } from "../../commons/services/notification.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"]
})
export class LoginComponent {
  cuilRegex = /2[0347]\d{9}$/;
  cuilFormControl = new FormControl("", [Validators.required, Validators.pattern(this.cuilRegex)]);
  passwordFormControl = new FormControl("", [Validators.required]);

  constructor(
    private translateService: TranslateService,
    private authenticationService: AuthenticationService,
    private jwtService: JwtService,
    private router: Router,
    private notificationService: NotificationService
  ) {
  }

  getCuilErrorMessage(): string {
    if (this.cuilFormControl.hasError("required")) {
      return this.translateService.instant("LOGIN.FORM.ERRORS.REQUIRED");
    } else {
      return this.cuilFormControl.hasError("pattern") ? this.translateService.instant("LOGIN.FORM.ERRORS.INVALID_CUIL") : "";
    }
  }

  getPasswordErrorMessage(): string {
    if (this.passwordFormControl.hasError("required")) {
      return this.translateService.instant("LOGIN.FORM.ERRORS.REQUIRED");
    } else {
      return "";
    }
  }

  login(): void {
    if (!this.cuilFormControl.valid || !this.passwordFormControl.valid) {
      return;
    }

    const loginDto: LoginDto = {
      cuil: this.cuilFormControl.value,
      password: this.passwordFormControl.value
    };

    this.authenticationService.login(loginDto).subscribe((authenticationDto: AuthenticationDto) => {
      this.jwtService.storeUser(authenticationDto.accessToken!);
      this.router.navigate([AppRoutes.home]).then();
    }, error => {
      console.log("Error: ", error);
      if (error.error === "SERVER_ERROR.USER_LOCKED") {
        this.router.navigate([AppRoutes.lockedUser]).then();
      }
      if (error.error) {
        this.notificationService.showError(error.error);
      }
    });
  }

  goToRegister(): void {
    this.router.navigate([AppRoutes.register]).then();
  }
}
