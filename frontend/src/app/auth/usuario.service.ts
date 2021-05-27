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

  public isAutenticado(): boolean{
    return this.autenticado;
  }

  public getStatusSubject (){
    return this.authStatusSubject.asObservable();
  }

  constructor(
    private httpClient: HttpClient,
    private router: Router
  ) {

  }

  public getToken (): string{
    return this.token;
  }

  public logout(): void{
    this.token = null;
    this.authStatusSubject.next(false);
    this.autenticado = false;
    /*clearTimeout(this.tokenTimer);*/
    this.idUsuario = null;
    this.removerDadosDeAutenticacao();
    this.router.navigate(['/']);
  }

  criarUsuario({nome, cpf, email, senha}){
    const dadosUsuario = {
      nome,
      cpf,
      email,
      senha,
    }
    this.axios.post(environment.backend_host+"/pessoa/insert", dadosUsuario)
      .then(() => {
        this.router.navigate(['/']);
      }).catch(() => {return 'erro'})
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
      return this.axios.post(environment.backend_host+"/pessoa/login", authData)
        .then(resposta => {
          console.log(JSON.stringify(resposta.data, null, 2));
          const dadosUsuario = resposta.data;
          if (this.token){
            /*const tempoValidadeToken = resposta.expiresIn;
            this.tokenTimer = setTimeout(() => {
              this.logout();
            }, tempoValidadeToken * 1000);
            console.log(resposta);*/
            this.autenticado = true;
            this.authStatusSubject.next(true);
            this.salvarDadosDeAutenticacao(dadosUsuario);
            this.router.navigate(['/']);
          }
        }).catch((erro) => {
          return 'erro';
        })
    } catch (erro) {
    }
  }

  private salvarDadosDeAutenticacao (resposta){
    localStorage.setItem('token', resposta.token)
    localStorage.setItem('usuario',resposta)
  }

  private removerDadosDeAutenticacao (){
    localStorage.removeItem ('token');
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

  private obterDadosDeAutenticacao(){
    const token = localStorage.getItem ('token');
    const idUsuario = localStorage.getItem ('usuario.id');
    if (token) {
      return {token: token, idUsuario: idUsuario}
    }
    return null;
  }

}
