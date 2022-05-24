import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { SplashScreenComponent } from "./splash-screen.component";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { MatIconModule } from "@angular/material/icon";


@NgModule({
  declarations: [
    SplashScreenComponent
  ],
  imports: [
    CommonModule,
    MatProgressSpinnerModule,
    MatIconModule
  ],
  exports: [
    SplashScreenComponent
  ]
})
export class SplashScreenModule {
}
