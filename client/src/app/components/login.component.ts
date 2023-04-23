import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginService } from '../services/login.service';
import { User } from '../models';
import { SharingService } from '../services/sharing.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  hide = true;
  loginForm!: FormGroup
  values$!: Subscription
  state$!: Subscription
  id!: string 
  username!: string
  password!: string
  user!: User
  constructor(private fb: FormBuilder, private router: Router,private loginSvc: LoginService, private shareSvc : SharingService) { }


 
  ngOnInit(): void {
    this.loginForm = this.createForm()  }

  createUser(){

    const formData = new FormData()
    formData.set("username", this.loginForm.value['username'])
    formData.set("password", this.loginForm.value['password']) 
    formData.set("email", this.loginForm.value['email'])


    this.loginSvc.createUser(formData).then(result => {
      this.id = result["userId"]

      this.username = result["username"]
      localStorage.setItem("userId", this.id)
      localStorage.setItem("username", this.username)
      this.shareSvc.sharingValue = result["userId"]
        this.router.navigate([`/home/${this.id}`])
      }).catch(error => {
      console.log(error)
    })
      
      this.loginForm = this.createForm()
  }

 

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control('', [ Validators.required ]),
      password: this.fb.control('', [ Validators.required ]),
      email: this.fb.control('', [Validators.required])
    })
  }
  





}
