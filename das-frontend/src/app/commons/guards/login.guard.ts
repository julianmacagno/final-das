import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { JwtService } from "../services/jwt.service";
import { AppRoutes } from "../enums/AppRoutes";

@Injectable({
  providedIn: "root"
})
export class LoginGuard implements CanActivate {
  constructor(
    private router: Router,
    private jwtService: JwtService
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.jwtService.isTokenValid()) {
      this.router.navigate([AppRoutes.home]).then();
      return false;
    } else {
      return true;
    }
  }
}
