import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomeComponent } from './components/home.component';
import { LoginComponent } from './components/login.component';
import { LoginService } from './services/login.service';
import { RouterModule, Routes } from '@angular/router';
import { ResultsComponent } from './components/results.component';
import { DetailsComponent } from './components/details.component';
import { BookService } from './services/book.service';
import { CommentService } from './services/comment.service';
import { AngularMaterialModule } from './angular-material.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { SharingService } from './services/sharing.service';

const appRoutes: Routes = [
  { path: '', component: LoginComponent,},
  { path: 'books/:keyword', component: ResultsComponent},
  { path: 'home/:id', component: HomeComponent },
  { path: 'book/:bookId', component: DetailsComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' },
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    ResultsComponent,
    DetailsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
    AngularMaterialModule,
    BrowserAnimationsModule
  ],

  providers: [LoginService, BookService, CommentService, SharingService],
  bootstrap: [AppComponent]
})
export class AppModule { }
