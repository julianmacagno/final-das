import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError, finalize } from "rxjs/operators";
import { Router } from "@angular/router";
import { JwtService } from "../services/jwt.service";
import { AuthenticationService } from "@codegen/rescueapp/api/authentication.service";
import { AppRoutes } from "../enums/AppRoutes";
import { NotificationService } from "../services/notification.service";

@Injectable({
  providedIn: "root"
})
export class AuthInterceptorService implements HttpInterceptor {
  flag = false;

  constructor(
    private jwtService: JwtService,
    private router: Router,
    private authenticationService: AuthenticationService,
    private notificationService: NotificationService
  ) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.includes("/login") || req.url.includes("/register") || req.url === "/" || req.url.includes("/assets")) {
      return next.handle(req.clone());
    }

    if (this.jwtService.getUser && this.jwtService.isTokenValid() && this.jwtService.isUserNextToExpire() && !this.flag) {
      this.refreshToken();
    }

    const token = this.jwtService.getUser!;
    const customRequest = req.clone({
      headers: req.headers.set("Authorization", `Bearer ${token}`)
    });

    return next.handle(customRequest).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status === 401 || err.status === 403) {
          this.notificationService.showError("SERVER_ERROR.SESSION_EXPIRED");
          this.router.navigateByUrl(AppRoutes.login).then();
        }
        return throwError(err);
      })
    );
  }

  refreshToken(): void {
    this.flag = true;
    console.log("fetching new token! ");
    this.authenticationService
      .refreshToken(this.jwtService.getUser!)
      .pipe(
        catchError(() => {
          this.jwtService.destroySession();
          return throwError(403);
        }),
        finalize(() => (this.flag = false))
      )
      .subscribe((data) => {
        this.jwtService.storeUser(data.accessToken!);
      });
  }
}
