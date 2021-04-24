import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms'

@Component({
  selector: 'app-post-inserir',
  templateUrl: './post-inserir.component.html',
  styleUrls: ['./post-inserir.component.css']
})
export class PostInserirComponent implements OnInit {

  onSalvarPost(form: NgForm) {
    const post = {
      localizacao: form.value.localizacao,
      tema: form.value.tema,
      texto: form.value.texto
    }
    console.log(post);
  }

  onAdicionarFoto(){
    console.log("adicionando foto...")
  }

  constructor() { }

  ngOnInit(): void {
  }

}
