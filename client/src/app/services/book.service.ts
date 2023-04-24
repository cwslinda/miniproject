import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, last, lastValueFrom } from "rxjs";
import { Book } from "../models";

@Injectable({
    providedIn: 'root'
})
export class BookService{
    
    constructor(private http: HttpClient){}

    url = "https://ignorant-north-production.up.railway.app";


    public getBooks(keyword: string): Promise<Book[]>{

        const params = new HttpParams().set("keyword", keyword)

        const headers = new HttpHeaders()
                        .set('content-type', 'application/json')
                        .set('Access-Control-Allow-Origin', '*')

        return lastValueFrom(this.http.get<Book[]>(`${this.url}/api/search`, {headers, params}))
    }


   public getSingleBookById(bookId: string): Promise<Book>{
    // const params = new HttpParams().set("id", bookId)

    const headers = new HttpHeaders()
      .set('content-type', 'application/json')
      .set('Access-Control-Allow-Origin', '*')

    return lastValueFrom(this.http.get<Book>(`${this.url}/api/book/${bookId}`, {headers})
    )
   }

   public getBooksFromUser(userId: string): Promise<any>{

            const headers = new HttpHeaders()
            .set('content-type', 'application/json')
            .set('Access-Control-Allow-Origin', '*')

      return lastValueFrom(this.http.get<any>(`${this.url}/api/user/${userId}`, {headers})
      
    )
   }

   public saveBook(form: Book, userId: string): Promise<any>{
    
    
                const headers = new HttpHeaders()
                .set('Access-Control-Allow-Origin', '*')
                .set('Accept', 'application/json')
            
           

                const data = new FormData()
                data.set("form", new Blob([JSON.stringify(form)], {
                  type: 'application/json'
              }));

           
               

              
          
        return lastValueFrom(
                    this.http.post<any>(`${this.url}/api/save/${userId}`, data, {headers})
                )
   }

   deleteBook(bookId: string, userId: string) {
    console.info("deleting");

    const params = new HttpParams().set("bookId", bookId).set("userId", userId)

    return firstValueFrom(
        this.http.delete<any>(`${this.url}/api/delete`, {params})     
    )              
}

}

