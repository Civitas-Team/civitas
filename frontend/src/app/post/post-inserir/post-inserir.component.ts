import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, NgForm, FormControl } from '@angular/forms'
import { User } from '../user.model'
import { Tema } from '../tema.model'
import { PostService } from '../post.service'
import { mimeTypeValidator } from './mime-type.validator'
@Component({
  selector: 'app-post-inserir',
  templateUrl: './post-inserir.component.html',
  styleUrls: ['./post-inserir.component.css']
})

export class PostInserirComponent implements OnInit {

  constructor(public postService: PostService) {}

  ngOnInit(): void {
    // **Usar para produção**
    // this.temas = this.postService.getListaTemas();
    // this.user = this.postService.getUser();
    // this.coordenada_user = this.user.localizacao
    this.form = new FormGroup({
      localizacao: new FormControl(null, {
        validators: [Validators.required]
      }),
      tema: new FormControl(null, {
        validators: [Validators.required]
      }),
      texto: new FormControl(null, {
        validators: [Validators.required]
      }),
      imagem: new FormControl(null, {
        validators: []
      })
    })
  }

  form: FormGroup;

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
    const arquivo = (event.target as HTMLInputElement).files[0]
    console.log(arquivo)
    this.form.patchValue({'imagem': arquivo})
    this.form.get('imagem').updateValueAndValidity()
    const reader = new FileReader()
    reader.onload = () => {
      this.previewImagem = reader.result as string
    }
    reader.readAsDataURL(arquivo)
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

  onSalvarPost() {
    if (this.form.invalid){
      return
    }

    const postdados = {
      localizacao: this.coordenada_user,
      tema: this.getIdTema(this.form.value.tema),
      texto: this.form.value.texto,
      imagem: this.form.value.imagem
    }

    // const dadosPost = new FormData()
    // dadosPost.append('localizacao', this.coordenada_user)
    // dadosPost.append('tema', this.getIdTema(this.form.value.tema).toString())
    // dadosPost.append('texto', this.form.value.texto)
    // dadosPost.append('imagem', this.form.value.imagem)

    const post = {
      body: postdados,
      idUser: {headers: {userID: 4}}
    }
    console.log('chegou em OnSalvarPost' + JSON.stringify(post.body, null, 2))
    this.postService.salvarPost(post)
    this.form.reset()
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
