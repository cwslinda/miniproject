import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom, last, lastValueFrom } from "rxjs";
import { Book, CommentResult } from "../models";

@Injectable({
    providedIn: 'root'
})
export class CommentService{

    url = "https://ignorant-north-production.up.railway.app";

    constructor(private http: HttpClient) { }


postComment(formData: FormData): Promise<any>{
    console.log("in service - postComment")

    console.log(formData)

    return firstValueFrom(
        this.http.post<any>(`${this.url}/api/comment/save`, formData)
    )
}


 getComments(bookId: string): Promise<CommentResult[]>{

    const params = new HttpParams().set("bookId", bookId)

    const headers = new HttpHeaders()
                    .set('content-type', 'application/json')
                    .set('Access-Control-Allow-Origin', '*')

    return lastValueFrom(this.http.get<CommentResult[]>(`${this.url}/api/${bookId}/comments`, {headers}))
}

getCommentsFromId(userId: string): Promise<CommentResult[]>{

    const params = new HttpParams().set("userId", userId)

    const headers = new HttpHeaders()
                    .set('content-type', 'application/json')
                    .set('Access-Control-Allow-Origin', '*')

    return lastValueFrom(this.http.get<CommentResult[]>(`${this.url}/api/user-comments/${userId}`, {headers}))
}

deleteComment(commentId: string): Promise<any>{
    const params = new HttpParams().set("commentId", commentId)

    return lastValueFrom(this.http.delete<any>(`${this.url}/api/comment/delete`, {params}))
    }
}