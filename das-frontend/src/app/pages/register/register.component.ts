import { Component } from "@angular/core";
import { AbstractControl, FormBuilder, FormControl, Validators } from "@angular/forms";
import { AuthenticationService } from "@codegen/rescueapp/api/authentication.service";
import { NotificationService } from "../../commons/services/notification.service";
import { Router } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { RegisterDto, User } from "@codegen/rescueapp/model/models";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"]
})
export class RegisterComponent {
  cuilRegex = /2[0347]\d{9}$/;

  checkPasswordConfirmation = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validationForm.controls.passwordFormControl.value) {
      return { passwordDifferent: true };
    }
    return {};
  };

  checkEmailConfirmation = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else if (control.value !== this.validationForm.controls.emailFormControl.value) {
      return { emailDifferent: true };
    }
    return {};
  };

  validationForm = this.fb.group({
    cuilFormControl: ["", [Validators.required, Validators.pattern(this.cuilRegex)]],
    nameFormControl: ["", [Validators.required]],
    emailFormControl: ["", [Validators.required, Validators.email]],
    repeatEmailFormControl: ["", [Validators.required, Validators.email, this.checkEmailConfirmation]],
    passwordFormControl: ["", [Validators.required, Validators.minLength(8)]],
    repeatPasswordFormControl: ["", [Validators.required, Validators.minLength(8), this.checkPasswordConfirmation]]
  });

  constructor(
    private authenticationService: AuthenticationService,
    private notificationService: NotificationService,
    private router: Router,
    private fb: FormBuilder
  ) {
  }

  getErrorMessage(formControl: AbstractControl): string {
    if (formControl.hasError("required")) {
      return "REGISTER.FORM.ERRORS.REQUIRED";
    } else if (formControl.hasError("pattern")) {
      return "REGISTER.FORM.ERRORS.INVALID_CUIL";
    } else if (formControl.errors?.minlength) {
      return "REGISTER.FORM.ERRORS.PASSWORD_TOO_SHORT";
    } else if (formControl.errors?.passwordDifferent) {
      return "REGISTER.FORM.ERRORS.PASSWORD_DIFFERENT";
    } else if (formControl.hasError("email")) {
      return "REGISTER.FORM.ERRORS.INVALID_EMAIL";
    } else if (formControl.errors?.emailDifferent) {
      return "REGISTER.FORM.ERRORS.EMAIL_DIFFERENT";
    } else {
      return "";
    }
  }

  register(): void {
    this.validationForm.controls.cuilFormControl.markAsTouched();
    this.validationForm.controls.nameFormControl.markAsTouched();
    this.validationForm.controls.emailFormControl.markAsTouched();
    this.validationForm.controls.repeatEmailFormControl.markAsTouched();
    this.validationForm.controls.passwordFormControl.markAsTouched();
    this.validationForm.controls.repeatPasswordFormControl.markAsTouched();

    if (!this.validationForm.valid) {
      return;
    }

    const registerDto: RegisterDto = {
      cuil: this.validationForm.controls.cuilFormControl.value,
      name: this.validationForm.controls.nameFormControl.value,
      email: this.validationForm.controls.emailFormControl.value,
      password: this.validationForm.controls.passwordFormControl.value,
      language: navigator.language
    };
    console.log("RegisterDto: ", registerDto);

    this.authenticationService.register(registerDto).subscribe((user: User) => {
      if (user) {
        this.notificationService.showSuccess("REGISTER.NOTIFICATION.SUCCESS", 15000);
        this.goToLogin();
      }
    }, error => {
      if (error.error) {
        this.notificationService.showError(error.error, 5000);
      }
      console.log("Error: ", error);
    });
  }

  goToLogin(): void {
    this.router.navigate([AppRoutes.login]).then();
  }
}
