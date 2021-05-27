import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {PostInserirComponent} from './post/post-inserir/post-inserir.component';
import { PostReadComponent } from './post/post-read/post-read.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';

const routes: Routes = [
    {path: '',component:LoginComponent},
    {path: 'pagPrincipal', component: PostReadComponent},
    {path: 'novoPost', component: PostInserirComponent},
    {path: 'signup', component: SignupComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
