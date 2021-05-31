import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthData } from './auth-data.model';
import { Subject } from 'rxjs'
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import axios from 'axios';
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private autenticado: boolean = false;
  private token: string;
  /*private tokenTimer: NodeJS.Timer;*/
  private authStatusSubject = new Subject <boolean>();
  private idUsuario: string;
  axios = axios;

  constructor(
    private httpClient: HttpClient,
    private router: Router
  ) {}

  public isAutenticado(): boolean{
    return this.autenticado;
  }

  public getStatusSubject (){
    return this.authStatusSubject.asObservable();
  }


  public getToken (): string{
    return this.token;
  }

  public logout() {
    this.httpClient.post(environment.backend_host+"/pessoa/logout", {},{headers: {Authorization: this.token, id: this.idUsuario}})
      .subscribe(() => {
        this.token = null;
        this.authStatusSubject.next(false);
        this.autenticado = false;
        this.idUsuario = null;
        this.removerDadosDeAutenticacao();
        this.router.navigate(['login']);
      })
  }

  criarUsuario({nome, cpf, email, senha}){
    const dadosUsuario = {
      nome,
      cpf,
      email,
      senha,
    }
    this.httpClient.post(environment.backend_host+"/pessoa/cadastroUsuario", dadosUsuario)
      .subscribe({
        next: () => {
          this.router.navigate(['login']);
        },
        error: () => {
          this.authStatusSubject.next(false);
        }
      });
  }

  public getIdUsuario(){
    return this.idUsuario;
  }

  async login (email: string, senha: string){
    const authData: AuthData = {
      email,
      senha,
    }
    try{
      this.httpClient.post<{token: string}>
        (environment.backend_host+"/pessoa/login", authData)
        .subscribe((resposta) => {
          const dadosUsuario = resposta
          if (dadosUsuario.token){
            this.autenticado = true;
            this.authStatusSubject.next(true);
            this.salvarDadosDeAutenticacao(dadosUsuario);
            this.router.navigate(['/']);
          }
        });
    } catch (erro) {
    }
  }

  private salvarDadosDeAutenticacao (resposta){
    localStorage.setItem('token', resposta.token)
    localStorage.setItem('idUsuario', resposta.id)
    localStorage.setItem('usuario', JSON.stringify(resposta))
  }

  private removerDadosDeAutenticacao (){
    localStorage.removeItem ('token');
    localStorage.removeItem ('idUsuario');
    localStorage.removeItem ('usuario');
  }

  public autenticarAutomaticamente (): void{
    const dadosAutenticacao = this.obterDadosDeAutenticacao();
    if (dadosAutenticacao){
      this.token = dadosAutenticacao.token;
      this.autenticado = true;
      this.idUsuario = dadosAutenticacao.idUsuario;
      this.authStatusSubject.next(true);
    }
  }

  public getUsuarioData() {
    return JSON.parse(localStorage.getItem('usuario'));
  }

  private obterDadosDeAutenticacao(){
    const token = localStorage.getItem ('token');
    const idUsuario = localStorage.getItem ('idUsuario');
    if (token) {
      return {token, idUsuario}
    }
    return null;
  }

  public async updateUsuario(usuario) {
    return this.httpClient.post(environment.backend_host + "/pessoa/login", usuario, { headers: { Authorization: this.getToken() }})
      .subscribe(() => 'update feito com sucesso')
  }

}
