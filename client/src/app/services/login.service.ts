import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { User } from "../models";



@Injectable({
    providedIn: 'root'
  })
export class LoginService {

    url = "https://ignorant-north-production.up.railway.app";


    constructor(private http: HttpClient) { }

    createUser(formData: FormData): Promise<any>{
            
        const headers = new HttpHeaders()
        .set('Access-Control-Allow-Origin', '*')

    return firstValueFrom(
        this.http.post<any>(`/api/register`, formData, {headers})
        )

    }

    
}