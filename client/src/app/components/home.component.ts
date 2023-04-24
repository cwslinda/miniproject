import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book, CommentResult, User } from '../models';
import { BookService } from '../services/book.service';
import { CommentService } from '../services/comment.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {


  panelOpenState = false;
  showFiller = false;
  user!: User
  form!: FormGroup
  param$!: Subscription
  userId!: string
  books!: Book[]
  comments!: CommentResult[]

  username: string = localStorage.getItem("username")!


  constructor(private fb: FormBuilder, private router: Router, private ar: ActivatedRoute, private bookSvc: BookService, private commentSvc: CommentService){}

  ngOnInit(): void {

    this.param$ = this.ar.params.subscribe(
      (params) => {
    
        this.userId = params['id']
        
        this.bookSvc.getBooksFromUser(this.userId)
          .then(result => {
            this.books = result['books']
            console.log(this.books)

          }).catch(error => {
            console.log(error)

          })

          this.commentSvc.getCommentsFromId(this.userId)
          .then(result => {
             console.log(result)
            this.comments = result
          }).catch(error => {
            console.log(error)
          })
       
      }
    )
    this.form = this.createForm();
  }

 
  search(){
    const keyword: string = this.form.value['keyword']
    console.log("search for", keyword)

    this.router.navigate([`/books/${keyword}`])
  }

  deleteBook(bookId: string){
    this.bookSvc.deleteBook(bookId, this.userId).then(result => {
      console.log(result)
      this.ngOnInit()
    }).catch(error => {
      console.log("unable to delete book due to: " + error)
    })
  }


  logout() : void {
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  }

  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control<string>("", [Validators.required])
    })

  }

  deleteComment(commentId: string){
  
    this.commentSvc.deleteComment(commentId).then(result => {
    console.log(result)
    this.ngOnInit()
    
  }).catch(error =>{
    console.log(error)

    })
  }
}
