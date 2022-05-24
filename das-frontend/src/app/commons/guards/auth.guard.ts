import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanLoad, Router, RouterStateSnapshot } from "@angular/router";
import { JwtService } from "../services/jwt.service";
import { AppRoutes } from "../enums/AppRoutes";

@Injectable({
  providedIn: "root"
})
export class AuthGuard implements CanActivate, CanLoad {
  constructor(private router: Router, private jwtService: JwtService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.jwtService.isTokenValid()) {
      return true;
    } else {
      this.jwtService.destroySession();
      this.router.navigate([AppRoutes.login]).then();
      return false;
    }
  }

  canLoad(): boolean {
    if (this.jwtService.isTokenValid()) {
      return true;
    } else {
      this.jwtService.destroySession();
      this.router.navigate([AppRoutes.login]).then();
      return false;
    }
  }
}
