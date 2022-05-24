import { Component } from "@angular/core";

@Component({
  selector: "app-content",
  template: `
    <app-splash-screen></app-splash-screen>
    <router-outlet></router-outlet>
  `,
  styles: []
})
export class ContentComponent {

  constructor() {
  }

}
