import { HttpClient } from '@angular/common/http';


export class PostService {

  constructor(httpClient: HttpClient) {}

  getLocalizacaoTexto({latitude, longitude}) {
    this.httpClient.get(`https://maps.googleapis.com/maps/api/geocode/json?${}`)
  }
}
