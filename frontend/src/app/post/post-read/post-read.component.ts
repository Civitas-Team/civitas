import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { Post } from '../post.model';
import { PostService } from '../post.service'
import { UsuarioService } from '../../auth/usuario.service'
import { environment } from '../../../environments/environment'
import { Form, FormControl, FormGroup } from '@angular/forms';
@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit, OnDestroy {

  form: FormGroup
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
  toggle = true;
  status = 'Enable';
  usuario_coordenada: string;
  usuario_cidade: string;
  private authStatusSubscription: Subscription;
  // post = {
  //   localizacao: 'localizacao'
  // }


  constructor(private postService: PostService, private usuarioService: UsuarioService) {}


  async ngOnInit() {
    this.form = new FormGroup({
      localizacao: new FormControl(null, {
        validators: []
      })
    })
    this.authStatusSubscription = this.usuarioService.getStatusSubject()
      .subscribe( async () => {
        await this.iniciar();
      });
    await this.iniciar();
  }

  ngOnDestroy(): void {
    this.authStatusSubscription.unsubscribe();
  }

  async iniciar() {
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

  funcaoTeste() {
    this.ngOnInit();
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

  enableDisableRule() {
    this.toggle = !this.toggle;
    this.status = this.toggle ? 'Enable' : 'Disable';
  }

  async getLocalizacao() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(async (position) => {
        const { latitude, longitude } = position.coords
        this.usuario_coordenada = `${latitude},${longitude}`
        const endereco = await this.postService.converteLocalizacaoTexto(this.usuario_coordenada)
        this.form.patchValue({localizacao: endereco.endereco})
        this.usuario_cidade = endereco.cidade
      });
      } else {
      return "Seu browser não suporta Geolocalização.";
    }
  }

  async onAtualizarLocalizacao() {
    const usuario_data = this.usuarioService.getUsuarioData();
    usuario_data.localizacao = this.usuario_coordenada;
    usuario_data.cidade = this.usuario_cidade;
    console.log(usuario_data);
    // await this.usuarioService.updateUsuario(usuario_data)
    await this.iniciar()
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
