import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "../../commons/enums/AppRoutes";
import { LoginComponent } from "./login.component";
import { LoginGuard } from "../../commons/guards/login.guard";

const routes: Routes = [
  {
    path: AppRoutes.loginBase,
    component: LoginComponent,
    runGuardsAndResolvers: "always",
    pathMatch: "full",
    canActivate: [LoginGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LoginRoutingModule {
}
