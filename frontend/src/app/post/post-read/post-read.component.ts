import { Component, OnInit } from '@angular/core';
import {PostService} from '../post.service'
@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit {

  postService = new PostService()
  isCarregando: boolean = false;

  posts

  img = "../../assets/imagem.jpg"
  post = {
    localizacao: 'localizacao'
  }

  constructor() { }

  async ngOnInit() {
    this.isCarregando = true
    this.posts = await this.postService.getPosts(1, {headers: {userID: 4}})
    this.isCarregando = false
  }

}
