import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {PostInserirComponent} from './post/post-inserir/post-inserir.component';
import { PostReadComponent } from './post/post-read/post-read.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';

import { AuthGuard } from './auth/auth.guard'

const routes: Routes = [
    {path: 'login',component: LoginComponent},
    {path: '', component: PostReadComponent, canActivate: [AuthGuard]},
    {path: 'novoPost', component: PostInserirComponent, canActivate: [AuthGuard]},
    {path: 'signup', component: SignupComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
    providers: [AuthGuard]
})
export class AppRoutingModule { }
