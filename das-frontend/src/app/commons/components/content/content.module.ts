import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ContentComponent } from "./content.component";
import { RouterModule } from "@angular/router";
import { SplashScreenModule } from "../splash-screen/splash-screen.module";


@NgModule({
  declarations: [
    ContentComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    SplashScreenModule
  ],
  exports: [
    ContentComponent
  ]
})
export class ContentModule {
}
