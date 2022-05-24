import { Component } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";
import { JwtService } from "./commons/services/jwt.service";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent {
  constructor(
    private translateService: TranslateService,
    private jwtService: JwtService
  ) {
    translateService.addLangs(["es", "en"]);
    translateService.setDefaultLang("es");

    const browserLang = translateService.getBrowserLang();
    translateService.use(browserLang.match(/es|en/) ? browserLang : "es");
  }

  isUserLogged(): boolean {
    return this.jwtService.isTokenValid();
  }

  title = "Rescue App";
}
