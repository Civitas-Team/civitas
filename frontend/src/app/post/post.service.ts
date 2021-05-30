import { Injectable } from '@angular/core';
import axios from 'axios'
import { User } from './user.model'
import { environment } from '../../environments/environment'
import { UsuarioService } from '../auth/usuario.service';

@Injectable({ providedIn: 'root'})
export class PostService {

  url = environment.backend_host
  axios = axios
  APIkey = environment.google_api_key
  user: User

  constructor(private usuarioService: UsuarioService) {}

  async getListaTemas() {
    return await this.axios.get(`${this.url}/tema/getAll`, {headers: {Authorization: this.usuarioService.getToken()}})
      .then((res) => {
       return res.data
    })
  }

  confirmarPost(idPost) {
    this.axios.post(`${this.url}/postagem/confirmarInfo/${idPost}`, {}, {headers: {Authorization: this.usuarioService.getToken()}})
  }

  // async getUser() {
  //   return await this.axios.get(`${this.url}/pessoa/getAll`)
  //     .then((res) => {
  //       return res.data[0]
  //     })
  // }

  async getUsuarioData() {
    return this.usuarioService.getUsuarioData();
  }

  salvarPost(postBody: {}) {
    this.axios.post(`${this.url}/postagem`, postBody, {headers: {Authorization: this.usuarioService.getToken()}})
  }

  async getPosts(page: number, userId: number){
    return this.axios.get(`${this.url}/postagem/getPosts?itensPerPage=5&currentPage=${page}`, {headers: {Authorization: this.usuarioService.getToken()}})
      .then((res) => {
      return res.data
    })
  }

  async converteLocalizacaoTexto(coordenada){
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${coordenada}&key=${this.APIkey}`
    return await this.axios.get(url)
      .then((res) => {
        const address_components = res["data"]["results"][0]["address_components"]
        return {
          endereco: `${address_components[1]["short_name"]}, ${address_components[2]["short_name"]} - ${address_components[3]["short_name"]}`,
          cidade: address_components[3]["short_name"]
        }
      })
  }

  // async getLocalizacao() {
  //   if (navigator.geolocation) {
  //     let coordenada = ''
  //     let endereco = ''
  //     let cidade = ''
  //     navigator.geolocation.getCurrentPosition((position) => {
  //       const { latitude, longitude } = position.coords
  //       this.posicao = `${latitude},${longitude}`
  //       // const { latitude, longitude } = position.coords
  //       // coordenada = `${latitude},${longitude}`
  //       // const localizacao = await this.converteLocalizacaoTexto(coordenada)
  //       // cidade = localizacao.cidade
  //       // endereco = localizacao.endereco
  //     });
  //     let variavel = this.posicao
  //     return {coordenada, endereco, cidade, variavel}
  //   } else {
  //     return "Seu browser não suporta Geolocalização.";
  //   }
  // }
}
