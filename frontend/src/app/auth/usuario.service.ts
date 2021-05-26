import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthData } from './auth-data.model';
import { Subject } from 'rxjs'
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import axios, { AxiosTransformer } from 'axios';
@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private autenticado: boolean = false;
  private token: string;
  /*private tokenTimer: NodeJS.Timer;*/
  private authStatusSubject = new Subject <boolean>();
  private idUsuario: string;
  private axios = axios;

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

  criarUsuario(email: string, senha: string){
    const authData: AuthData = {
      email: email,
      senha: senha
    }
    this.httpClient.post(environment.backend_host+"/pessoa/insert", authData).
      subscribe({
        //vamos para página principal quando o usuário é criado com sucesso
        next: () => this.router.navigate(['/']),
        //notificamos todos os componentes que não há usuário autenticado
        error: () => this.authStatusSubject.next(false)
      });
  }

  public getIdUsuario(){
    return this.idUsuario;
  }

  login (email: string, senha: string){
    const authData: AuthData = {
      email: email,
      senha: senha
    }
    this.axios.get<{ token: string, expiresIn: number, idUsuario:string}>
    (environment.backend_host+"/pessoa/login", {headers: {email: email,
      senha: senha}}).
      then(resposta => {
         const dados = resposta.data;
        this.token = dados.token;
        if (this.token){
          /*const tempoValidadeToken = resposta.expiresIn;
          this.tokenTimer = setTimeout(() => {
            this.logout();
          }, tempoValidadeToken * 1000);
          console.log(resposta);*/
          this.autenticado = true;
          this.idUsuario = dados.idUsuario;
          this.authStatusSubject.next(true);
          this.salvarDadosDeAutenticacao(resposta);
          this.router.navigate(['/']);
        }
      })
  }

  private salvarDadosDeAutenticacao (resposta){
    localStorage.setItem ('usuario',resposta)
  }

  private removerDadosDeAutenticacao (){
    localStorage.removeItem ('token');
    localStorage.removeItem ('idUsuario');
  }

  /*public autenticarAutomaticamente (): void{
    const dadosAutenticacao = this.obterDadosDeAutenticacao();
    if (dadosAutenticacao){
      const agora = new Date();
      const diferenca = dadosAutenticacao.validade.getTime() - agora.getTime();
      if (diferenca > 0){
        this.token = dadosAutenticacao.token;
        this.autenticado = true;
        this.idUsuario = dadosAutenticacao.idUsuario;
        this.tokenTimer = setTimeout(() => {
          this.logout();
        }, diferenca);
        this.authStatusSubject.next(true);
      }
    }
  }*/

  private obterDadosDeAutenticacao(){
    const token = localStorage.getItem ('token');
    
    const idUsuario = localStorage.getItem ('idUsuario');
    if (token) {
      return {token: token, idUsuario: idUsuario}
    }
    return null;
  }

}