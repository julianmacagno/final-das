import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { HomeComponent } from "./home.component";

const routes: Routes = [
  {
    path: AppRoutes.homeBase,
    component: HomeComponent,
    runGuardsAndResolvers: "always",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
