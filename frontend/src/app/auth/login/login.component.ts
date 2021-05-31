import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UsuarioService } from '../usuario.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})

export class LoginComponent implements OnInit {

  isCarregando: boolean = false;

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {}


  async onLogin (form: NgForm){
    if (form.invalid) return;

    const resposta = await this.usuarioService.login(form.value.email, form.value.senha);

    if (resposta != null) {
    }
  }
}
