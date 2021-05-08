import { Injectable } from '@angular/core';
import axios from 'axios'
import { User } from './user.model'
import { environment } from '../../environments/environment'

@Injectable({ providedIn: 'root'})
export class PostService {

  url = 'https://civitasapi.herokuapp.com'
  axios = axios
  APIkey = environment.google_api_key
  user: User

  async getListaTemas() {
    return await this.axios.get(`${this.url}/tema/getAll`).then((res) => {
       return res.data
    })
  }

  async getUser() {
    return await this.axios.get(`${this.url}/pessoa/getAll`).then((res) => {
      return res.data[0]
    })
  }

  salvarPost(post: { body: {}; idUser: {}; }) {
    this.axios.post(`${this.url}/postagem`, post.body, post.idUser)
  }

  async getPosts(page, userId) {
    return await this.axios.get(`${this.url}/postagem/getPosts?itensPerPage=6&currentPage=${page}`, userId).then((res) => {
      return res.data.data
    })
  }

  async converteLocalizacaoTexto(coordenada){
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${coordenada}&key=${this.APIkey}`
    return await this.axios
      .get(url)
      .then((res) => {
        const address_components = res["data"]["results"][0]["address_components"]
        return `${address_components[1]["short_name"]}, ${address_components[2]["short_name"]} - ${address_components[3]["short_name"]}`
      })
  }
}
