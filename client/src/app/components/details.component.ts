import { Component, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book, CommentResult, User } from '../models';
import { BookService } from '../services/book.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { CommentService } from '../services/comment.service';
import { SharingService } from '../services/sharing.service';
import {Location} from '@angular/common';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent {

  readmore: boolean = true

 
  isNotUser: boolean = false
  book!: Book
  param$!: Subscription
  bookId!: string
  comments!: CommentResult[]
  userId!: string 
  commentForm!: FormGroup
  commentId!: string
  bookTitle!: string
  commentUserId!: string

  id = localStorage.getItem("userId")!

  constructor(private ar: ActivatedRoute, private location: Location, private router: Router, private bookSvc: BookService, private commentSvc: CommentService, private fb: FormBuilder, private sharingService : SharingService) {}
  
  ngOnInit(): void {
    this.commentForm = this.createForm();
    this.param$ = this.ar.params.subscribe(
      (params) => {
        console.log(params)
        this.bookId = params['bookId']
       

        this.bookSvc.getSingleBookById(this.bookId)
          .then(result => {
            this.book = result
          }).catch(error => {
            console.log(error)
          })


          this.commentSvc.getComments(this.bookId)
          .then(result => {
            this.comments = result
          }).catch(error => {
            console.log(error)
          })
      }
    )
  }

  deleteComment(commentId: string){
  
      this.commentSvc.deleteComment(commentId).then(result => {
      console.log(result)
      this.ngOnInit()
      
    }).catch(error =>{
      console.log(error)

    })
  }

 


  processForm(){
    const book = this.book
    this.bookSvc.saveBook(book, this.id)
      .then(result =>{
        console.log(book)
        this.router.navigate([`/home/${this.id}`])
      }).catch(error => {
        console.error(error)
      })
   
  }

  logout() : void {
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  }

  processCommentForm(){
   const formData = new FormData();
   formData.set("userId", this.id)
   formData.set("bookId", this.bookId)
   formData.set("bookTitle", this.book.title)
   formData.set("comment", this.commentForm.value["comment"])
   formData.set("commentTitle", this.commentForm.value["title"])

   this.commentSvc.postComment(formData).then(result => {
    this.commentId = result["commentId"]
    console.log("details components")
    this.ngOnInit()
   }).catch((error: any) =>{
    console.log(error)
   })

   this.commentForm = this.createForm();
  }


  createForm(): FormGroup {
    return this.fb.group({
      title: this.fb.control<string>(''),
      comment: this.fb.control<string>('')
    })
  }

  back(){
    if(this.id != null){
      this.location.back()
    } else {
      this.router.navigate([`/home`])

    }

  }

}
