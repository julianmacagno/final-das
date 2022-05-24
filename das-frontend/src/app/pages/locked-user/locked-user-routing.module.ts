import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "src/app/commons/enums/AppRoutes";
import { LockedUserComponent } from "./locked-user.component";

const routes: Routes = [
  {
    path: AppRoutes.lockedUserBase,
    component: LockedUserComponent,
    runGuardsAndResolvers: "always",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LockedUserRoutingModule {
}
