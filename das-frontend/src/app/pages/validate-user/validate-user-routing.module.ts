import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { ValidateUserComponent } from "./validate-user.component";

const routes: Routes = [
  {
    path: AppRoutes.validateUserBase,
    component: ValidateUserComponent,
    runGuardsAndResolvers: "always",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ValidateUserRoutingModule {
}
