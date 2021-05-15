import { Component, OnInit } from '@angular/core';
import { Post } from '../post.model';
import {PostService} from '../post.service'

@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit {

  infiniteScrollDistance: number = 1
  infiniteScrollThrottle: number = 50
  postService = new PostService()
  isCarregando: boolean = false;
  isPrimeiroLoading: boolean = true
  userId: number = 4
  totalPaginas: number
  pagina: number = 1
  fim: boolean = false
  postImagem: string
  posts: Post[]

  img = "../../assets/imagem.jpg"
  // post = {
  //   localizacao: 'localizacao'
  // }

  constructor() { }

  async ngOnInit() {
    this.isCarregando = true
    const respostaPosts = await this.postService.getPosts(1, this.userId)
    this.totalPaginas = respostaPosts.totalPages
    this.posts = respostaPosts.data
    console.log(this.posts)
    this.isPrimeiroLoading = false
    this.isCarregando = false
  }

  adicionaZero(numero: number | string){
    if (numero <= 9)
      return "0" + numero;
    else
      return numero;
  }

  formataData(data: string){
    let dataPost = new Date(data);
    return (this.adicionaZero(dataPost.getDate().toString()) +
            "/" + (this.adicionaZero(dataPost.getMonth()+1).toString()) +
            "/" + dataPost.getFullYear());
  }

  async onScroll() {
    console.log('adicionando mais items...')
    if(this.pagina >= this.totalPaginas) {
      this.fim = true
      return;
    }
    this.isCarregando = true
    this.pagina++
    const respostaPosts = await this.postService.getPosts(this.pagina, this.userId)
    this.posts = this.posts.concat(respostaPosts.data)
    console.log(this.posts)
    this.isCarregando = false
  }

  // renderizarImagem(arquivo: File) {
  //   const reader = new FileReader()
  //   const imagem = reader.onload = () => {
  //     return reader.result as string
  //   }
  //   reader.readAsDataURL(arquivo)
  //   return imagem;
  // }
}
