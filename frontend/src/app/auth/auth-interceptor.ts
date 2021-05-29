import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UsuarioService } from './usuario.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private usuarioService: UsuarioService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.usuarioService.getToken();
    console.log('passou no interceptor', token);
    if (token) {
      const copia = req.clone({
        headers: req.headers.set('Authorization', token),
      });
      return next.handle(copia);
    }
    return next.handle(req.clone());
  }
}
