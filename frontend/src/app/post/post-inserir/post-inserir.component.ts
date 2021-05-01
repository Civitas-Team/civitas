import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms'
import { User } from '../user.model'
import { Tema } from '../tema.model'
import { PostService } from '../post.service'
@Component({
  selector: 'app-post-inserir',
  templateUrl: './post-inserir.component.html',
  styleUrls: ['./post-inserir.component.css']
})

export class PostInserirComponent implements OnInit {

  constructor(public postService: PostService) {}

  ngOnInit(): void {
    // this.temas = this.postService.getListaTemas();
    // this.user = this.postService.getUser();
    // this.coordenada_user = this.user.localizacao
  }

  // Usar para desenvolvimento
  user = {
    nome: "Nome do usuário",
    localizacao: "Localizacao do usuário",
    imagem: "../../assets/profile.png"
  }

  temas = [
    {
      id: 1,
      nome: "Covid-19"
    },
    {
      id: 2,
      nome: "Doação"
    },
    {
      id: 3,
      nome: "Hospital"
    },
  ]


  // Usar para produção
  // user: User
  // temas: Tema[]

  previewImagem: string
  coordenada_user: string



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
      body: {
        corpo: form.value.texto,
        imagem: this.user.imagem,
        temaId: this.getIdTema(form.value.tema),
        localizacao: this.coordenada_user,
      },
      idUser: {headers: {userID: 4}}
    }
    this.postService.salvarPost(post)
  }


  getLocalizacao() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(position => {
        const { latitude, longitude } = position.coords
        this.coordenada_user = `${latitude},${longitude}`
        this.postService.converteLocalizacaoTexto({ latitude, longitude })
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
