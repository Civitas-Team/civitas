import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root'})
export class PostService {

  httpClient: HttpClient
  APIkey: string = "chaveapi"
  endereco: string
  constructor(httpClient: HttpClient) {}

  getLocalizacaoTexto(latitude: string, longitude: string): string {
    const resposta = this.httpClient.get<{results: [{formatted_address: string}]}>(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${this.APIkey}`)
    return resposta["results"][0].formatted_address
  }
}
