import { Injectable } from "@angular/core";
import { MatSnackBar, MatSnackBarConfig } from "@angular/material/snack-bar";
import { TranslateService } from "@ngx-translate/core";

@Injectable({
  providedIn: "root"
})
export class NotificationService {
  constructor(
    private snackBar: MatSnackBar,
    private translateService: TranslateService
  ) {
  }

  public showSuccess(message: string, duration: number = 5000): void {
    this.snackBar.open(this.translateService.instant(message), "", {
      duration,
      panelClass: ["snackbar-success"],
      verticalPosition: "bottom",
      horizontalPosition: "center"
    } as MatSnackBarConfig);
  }

  public showError(message: string, duration: number = 5000): void {
    this.snackBar.open(this.translateService.instant(message), "", {
      duration,
      panelClass: ["snackbar-error"],
      verticalPosition: "bottom",
      horizontalPosition: "center"
    } as MatSnackBarConfig);
  }

  public showWarning(message: string, duration: number = 5000): void {
    this.snackBar.open(this.translateService.instant(message), "", {
      duration,
      panelClass: ["snackbar-warn"],
      verticalPosition: "bottom",
      horizontalPosition: "center"
    } as MatSnackBarConfig);
  }
}
