import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ValidateUserRoutingModule } from "./validate-user-routing.module";
import { ValidateUserComponent } from "./validate-user.component";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { TranslateModule } from "@ngx-translate/core";
import { SplashScreenModule } from "../../commons/components/splash-screen/splash-screen.module";

@NgModule({
  declarations: [
    ValidateUserComponent
  ],
  imports: [
    CommonModule,
    ValidateUserRoutingModule,
    MatCardModule,
    MatButtonModule,
    TranslateModule,
    SplashScreenModule
  ]
})
export class ValidateUserModule {
}
