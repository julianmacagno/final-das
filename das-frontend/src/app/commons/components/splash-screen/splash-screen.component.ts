import { Component, EventEmitter, OnInit, Output } from "@angular/core";

@Component({
  selector: "app-splash-screen",
  templateUrl: "./splash-screen.component.html",
  styleUrls: ["./splash-screen.component.scss"]
})
export class SplashScreenComponent implements OnInit {
  windowWidth = "";
  showSplash = true;
  @Output() animationEnd = new EventEmitter<void>();

  ngOnInit(): void {
    setTimeout(() => {
      this.windowWidth = "-" + window.innerWidth + "px";
      setTimeout(() => {
        this.showSplash = !this.showSplash;
        this.animationEnd.emit();
      }, 500);
    }, 2000);
  }

}
