import { Injectable } from '@angular/core';
import axios from 'axios'
import { Tema } from './tema.model'
import { User } from './user.model'


@Injectable({ providedIn: 'root'})
export class PostService {

  axios = axios
  APIkey = "CHAVE_DA_API"
  temas: Tema[]
  user: User

  getListaTemas(): Tema[] {
    const temas = this.axios.get('http://localhost:8080/tema/getAll').then((res) => {
       this.temas = res.data
    })
    return this.temas
  }

  getUser() {
    this.axios.get('http://localhost:8080/pessoa/getAll').then((res) => {
      this.user = res.data[0]
    })
    return this.user
  }


  converteLocalizacaoTexto({latitude, longitude}){
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${this.APIkey}`
    this.axios
      .get(url)
      .then((res) => {
        const address_components = res["data"]["results"][0]["address_components"]
        this.user.localizacao = `${address_components[1]["short_name"]}, ${address_components[2]["short_name"]} - ${address_components[3]["short_name"]}`
      })
  }

  salvarPost(post: { body: {}; idUser: {}; }) {
    this.axios.post('http://localhost:3000/api/clientes', post.body, post.idUser)
  }

}
