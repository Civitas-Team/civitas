import { Component, OnInit } from '@angular/core';
import { PostService } from '../post/post.service'

@Component({
  selector: 'app-atualiza-localizacao',
  templateUrl: './atualiza-localizacao.component.html',
  styleUrls: ['./atualiza-localizacao.component.css']
})
export class AtualizaLocalizacaoComponent implements OnInit {

  constructor(private postService: PostService) { }

  ngOnInit(): void {
  }

  getLocalizacao() {
    // this.postService.getLocalizacao()
  }

}
