import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {PostInserirComponent} from './post/post-inserir/post-inserir.component';
import { PostReadComponent } from './post/post-read/post-read.component';

const routes: Routes = [
    {path: '',component:PostReadComponent},
    {path: 'novoPost', component: PostInserirComponent}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }