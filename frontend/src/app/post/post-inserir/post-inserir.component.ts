import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms'
import axios from 'axios'
import { User } from '../user.model'
import { Tema } from '../tema.model'
@Component({
  selector: 'app-post-inserir',
  templateUrl: './post-inserir.component.html',
  styleUrls: ['./post-inserir.component.css']
})

export class PostInserirComponent implements OnInit {

  constructor() {}

  ngOnInit(): void {
    this.getListaTemas()
    this.getUser()
  }

  axios = axios
  APIkey = "INSERIR_API_GOOGLE_MAPS"
  previewImagem: string
  user: User
  cord: string
  temas: Tema[]

  config = {
    headers: {
      userID: 4
    }
  }

  img: string = '../../assets/profile.png'

  getListaTemas() {
    this.axios.get('https://cors-anywhere.herokuapp.com/https://civitasapi.herokuapp.com/tema/getAll').then((res) => {
      this.temas = res.data
      console.log(this.temas)
    })
  }

  getUser() {
    this.axios.get('https://cors-anywhere.herokuapp.com/https://civitasapi.herokuapp.com/pessoa/getAll').then((res) => {
      this.user = res.data[0]
      this.cord = this.user.localizacao
    })
  }

  onImagemSelecionada(event: Event) {
    const imagem = (event.target as HTMLInputElement).files[0]
    const reader = new FileReader()
    reader.onload = () => {
      this.previewImagem = reader.result as string
    }
    reader.readAsDataURL(imagem)
  }

  getIdTema(valor: string){
    let id: number = 0
    this.temas.forEach((element) => {
      if (element.nome === valor) {
        id = element.id
      }
    })
    return id
  }

  onSalvarPost(form: NgForm) {
    const post = {
      corpo: form.value.texto,
      imagem: this.img,
      temaId: this.getIdTema(form.value.tema),
      localizacao: this.cord,
    }
    console.log(post)
    this.axios.post('https://cors-anywhere.herokuapp.com/https://civitasapi.herokuapp.com/postagem', post, this.config)
  }

  getLocalizacaoTexto({latitude, longitude}){
    this.cord = `${latitude},${longitude}`
    const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${this.APIkey}`
    this.axios
      .get(url)
      .then((res) => {
        console.log(res)
        const address_components = res["data"]["results"][0]["address_components"]
        this.user.localizacao = `${address_components[1]["short_name"]}, ${address_components[2]["short_name"]} - ${address_components[3]["short_name"]}`
        console.log(this.user.localizacao)
      })
  }

  getLocalizacao() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
        const { latitude, longitude } = position.coords
        this.getLocalizacaoTexto({ latitude, longitude })
      });
    } else {
    this.user.localizacao = "Seu browser não suporta Geolocalização.";}
  }

  showErrorLocalizacao(error) {
    switch(error.code) {
      case error.PERMISSION_DENIED:
        this.user.localizacao = "Usuário rejeitou exibir sua localização."
        break;
      case error.POSITION_UNAVAILABLE:
        this.user.localizacao = "Localização indisponível."
        break;
      case error.TIMEOUT:
        this.user.localizacao = "A requisição expirou."
        break;
      case error.UNKNOWN_ERROR:
        this.user.localizacao = "Algum erro desconhecido aconteceu."
        break;
      }
  }

}
