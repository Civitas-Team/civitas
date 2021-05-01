import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit {

  img = "../../assets/profile.png"
  post = {
    localizacao: 'localizacao'
  }
  previewImagem = ''

  constructor() { }

  ngOnInit(): void {
  }

}
