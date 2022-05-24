import { Injectable } from "@angular/core";
import { JwtHelperService } from "@auth0/angular-jwt";

@Injectable({
  providedIn: "root"
})
export class JwtService {
  readonly sessionKey = "currentUser";
  helper = new JwtHelperService();

  constructor() {
  }

  get getUser(): string | null {
    return sessionStorage.getItem(this.sessionKey);
  }

  public storeUser(token: string): void {
    sessionStorage.setItem(this.sessionKey, token);
  }

  public getDecodedToken(): any {
    return this.helper.decodeToken(this.getUser!);
  }

  public destroySession(): void {
    sessionStorage.clear();
  }

  public getSubFromToken(): string {
    return this.getDecodedToken().sub;
  }

  public getNameFromToken(): string {
    return this.getDecodedToken().name;
  }

  public getCuilFromToken(): string {
    return this.getDecodedToken().cuil;
  }

  public getExpFromToken(): number {
    return this.getDecodedToken().exp;
  }

  public getUserIdFromToken(): number {
    return this.getDecodedToken().userId;
  }

  public isTokenExpired(): boolean {
    return this.helper.isTokenExpired(this.getUser!);
  }

  public isTokenValid(): boolean {
    return this.getUser !== null && !this.isTokenExpired();
  }

  public isUserNextToExpire(): boolean {
    return this.helper.isTokenExpired(this.getUser!, 150);
  }
}
