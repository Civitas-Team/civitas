import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UsuarioService } from '../usuario.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, OnDestroy {

  isCarregando: boolean = false;
  private authObserver: Subscription;

  constructor(private usuarioService: UsuarioService) { }

  ngOnInit(): void {
    this.authObserver = this.usuarioService.getStatusSubject()
      .subscribe(
        authStatus => this.isCarregando = false
      )
  }

  onSignup(form: NgForm): void {
    if (form.invalid) return;
    const dadosUsuario = {
      nome: form.value.nome,
      cpf: form.value.cpf,
      email: form.value.email,
      senha: form.value.senha,
    }
    this.usuarioService.criarUsuario(dadosUsuario);
  }

  ngOnDestroy(): void {
    this.authObserver.unsubscribe();
  }

}
