import { Injectable } from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class GeolocationService {

  constructor() {
  }

  public getGeoLocation(): Promise<GeolocationCoordinates> {
    return new Promise((resolve, reject) => {
      navigator.geolocation.getCurrentPosition((geolocationPosition: GeolocationPosition) => {
        resolve(geolocationPosition.coords);
      }, (geolocationPositionError: GeolocationPositionError) => {
        reject(geolocationPositionError.code + " - " + geolocationPositionError.message);
      }, {
        enableHighAccuracy: true,
        maximumAge: 0
      });
    });
  }
}
