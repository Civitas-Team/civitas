import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http'
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export class ErroInterceptor implements HttpInterceptor{

  intercept(req: HttpRequest<any>, next: HttpHandler){
    return next.handle(req).pipe(
      catchError((erro: HttpErrorResponse) => {
        console.log('error interceptor' + JSON.stringify(erro));
        return throwError(erro);
      })
    )
  }
}
