import { NgModule } from "@angular/core";
import { ExtraOptions, RouterModule, Routes } from "@angular/router";
import { AppRoutes } from "./commons/enums/AppRoutes";
import { ContentComponent } from "./commons/components/content/content.component";
import { AuthGuard } from "./commons/guards/auth.guard";

const routes: Routes = [
  {
    path: AppRoutes.appBase,
    component: ContentComponent,
    children: [
      { path: AppRoutes.loginBase, pathMatch: "full", redirectTo: AppRoutes.login },
      {
        path: AppRoutes.login,
        loadChildren: () => import("./pages/login/login.module").then((m) => m.LoginModule),
        runGuardsAndResolvers: "always"
      },
      {
        path: AppRoutes.register,
        loadChildren: () => import("./pages/register/register.module").then((m) => m.RegisterModule),
        runGuardsAndResolvers: "always"
      },
      {
        path: AppRoutes.home,
        loadChildren: () => import("./pages/home/home.module").then((m) => m.HomeModule),
        canLoad: [AuthGuard],
        runGuardsAndResolvers: "always"
      },
      {
        path: AppRoutes.validateUser,
        loadChildren: () => import("./pages/validate-user/validate-user.module").then((m) => m.ValidateUserModule),
        runGuardsAndResolvers: "always"
      },
      {
        path: AppRoutes.lockedUser,
        loadChildren: () => import("./pages/locked-user/locked-user.module").then((m) => m.LockedUserModule),
        runGuardsAndResolvers: "always"
      }
    ]
  }
];

const config: ExtraOptions = {
  onSameUrlNavigation: "reload",
  scrollPositionRestoration: "enabled"
};

@NgModule({
  imports: [RouterModule.forRoot(routes, config)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
