import { Component, OnDestroy, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable, Subscription } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { UsuarioService } from '../auth/usuario.service';

@Component({
  selector: 'app-root-nav',
  templateUrl: './root-nav.component.html',
  styleUrls: ['./root-nav.component.css']
})
export class RootNavComponent implements OnInit, OnDestroy {

  private authObserver: Subscription;
  public autenticado: boolean = false;

  constructor(
    private breakpointObserver: BreakpointObserver,
    private usuarioService: UsuarioService,
    ) {}

    ngOnInit(): void {
      this.autenticado = this.usuarioService.isAutenticado();
      this.authObserver =
        this.usuarioService.getStatusSubject()
          .subscribe((autenticado) => {
            this.autenticado = autenticado;
          })
    }

    ngOnDestroy(): void {
      this.authObserver.unsubscribe();
    }


  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  onLogout() {
    this.usuarioService.logout();
  }


}
