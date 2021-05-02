import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-post-read',
  templateUrl: './post-read.component.html',
  styleUrls: ['./post-read.component.css']
})
export class PostReadComponent implements OnInit {

  posts = [
    {
      corpo: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Alias quam commodi pariatur quia praesentium maxime animi perferendis quasi obcaecati vel necessitatibus ratione, ipsa optio dolor dolore. Error modi minima obcaecati labore delectus sed tenetur consectetur aut, molestiae doloremque repellendus pariatur aliquam dolorum quis aliquid magnam alias praesentium nostrum, accusantium excepturi.",
      localizacao: "cordenadas",
      pessoa: {
        nome: "John Wick",
        imagem: "../../assets/profile.png",
      },
      tema: {
        nome: "Caso de Covid"
      },
      date: "2021-04-26T00:10:52.120+00:00"
    },
    {
      corpo: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Alias quam commodi pariatur quia praesentium maxime animi perferendis quasi obcaecati vel necessitatibus ratione, ipsa optio dolor dolore. Error modi minima obcaecati labore delectus sed tenetur consectetur aut, molestiae doloremque repellendus pariatur aliquam dolorum quis aliquid magnam alias praesentium nostrum, accusantium excepturi.",
      localizacao: "cordenadas",
      pessoa: {
        nome: "John Wick",
        imagem: "../../assets/profile.png",
      },
      tema: {
        nome: "Caso de Covid"
      },
      date: "2021-04-26T00:10:52.120+00:00"
    }
  ]



  img = "../../assets/imagem.jpg"
  post = {
    localizacao: 'localizacao'
  }

  constructor() { }

  ngOnInit(): void {
  }

}
