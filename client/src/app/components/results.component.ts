import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Book } from '../models';
import { BookService } from '../services/book.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
 

  books!: Book[]
  keyword!: string

  param$!: Subscription

  constructor(private ar: ActivatedRoute, private svc: BookService, private router: Router){}




  ngOnInit(): void {
    const userId = localStorage.getItem("userId");
    console.log(userId)
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
      }
    )
    }

}
