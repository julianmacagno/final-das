import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { RegisterComponent } from "./register.component";

const routes: Routes = [
  {
    path: AppRoutes.registerBase,
    component: RegisterComponent,
    runGuardsAndResolvers: "always",
    pathMatch: "full"
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegisterRoutingModule {
}
