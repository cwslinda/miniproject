import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book } from '../models';
import { BookService } from '../services/book.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
 

  books!: Book[]
  keyword!: string
  form!:FormGroup

  param$!: Subscription
  userId = localStorage.getItem("userId")


  constructor(private ar: ActivatedRoute, private svc: BookService, private router: Router, private fb: FormBuilder){}




  ngOnInit(): void {
    this.param$ = this.ar.params.subscribe(
      (params) => {
        this.keyword = params['keyword']

        this.svc.getBooks(this.keyword)
          .then(
            result => {
              console.log(result)
              this.books = result
            }).catch(error => {
              console.log(error)
            })
      })
      this.form = this.createForm();
    }


  logout() : void {
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
  }


  search(){
    const keyword: string = this.form.value['keyword']
    console.log("search for", keyword)

    this.router.navigate([`/books/${keyword}`])
  }

  createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control<string>("", [Validators.required])
    })

  }

}
