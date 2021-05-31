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

  async getUsuarioData() {
    return this.usuarioService.getUsuarioData();
  }

  toBase64 = (file: File) => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = error => reject(error);
  });

  async salvarPost(postBody) {
    if (postBody.imagem) {
      postBody.imagem = await this.toBase64(postBody.imagem);
    }
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
}
