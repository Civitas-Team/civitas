import { Component, OnInit } from '@angular/core';
import { Post } from '../post.model';
import {PostService} from '../post.service'
import { environment } from '../../../environments/environment'
@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit {

  apiKey: string = environment.google_api_key
  infiniteScrollDistance: number = 1
  infiniteScrollThrottle: number = 50
  isCarregando: boolean = false;
  isPrimeiroLoading: boolean = true
  userId: number = 4
  totalPaginas: number
  pagina: number = 1
  fim: boolean = false
  postImagem: string
  posts: Post[]
  localizacao: string = "-23.532466,-46.529625"
  img = "../../assets/imagem.jpg"
  semPosts = true;
  // post = {
  //   localizacao: 'localizacao'
  // }

  constructor(private postService: PostService) {}

  async ngOnInit() {
    this.isCarregando = true
    const respostaPosts = await this.postService.getPosts(1, this.userId)
    this.totalPaginas = respostaPosts.totalPages
    this.posts = respostaPosts.data
    this.semPosts = (this.posts.length > 0) ? false : true;
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
    if(this.pagina >= this.totalPaginas) {
      this.fim = true
      return;
    }
    this.isCarregando = true
    this.pagina++
    const respostaPosts = await this.postService.getPosts(this.pagina, this.userId)
    this.posts = this.posts.concat(respostaPosts.data)
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
