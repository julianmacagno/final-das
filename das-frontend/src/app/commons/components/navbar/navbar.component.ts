import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { AppRoutes } from "../../enums/AppRoutes";
import { JwtService } from "./../../services/jwt.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"]
})
export class NavbarComponent {


  constructor(
    private jwtService: JwtService,
    private router: Router
  ) {
  }

  navigateHome(): void {
    this.router.navigate([AppRoutes.home]).then();
  }

  logout(): void {
    this.jwtService.destroySession();
    this.router.navigate([AppRoutes.login]).then();
  }

  validToken(): boolean {
    return this.jwtService.isTokenValid();
  }
}
