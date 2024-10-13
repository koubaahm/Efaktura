import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { RegisterRequest } from '../models/RegisterRequest';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
    
  baseurl = 'http://localhost:8080/api/v1/auth'
  

  constructor(private http:HttpClient,private router:Router) { 
    
  }

  isUserAuthenticated():boolean{
    if (localStorage.getItem ("accesstoken")){
      return true;
    }
    this.router.navigate(["/login"])
return false;
  }

  login(authenticationRequest : AuthenticationRequest):Observable<AuthenticationResponse>{
    const url=this.baseurl+"/authenticate"
    return this.http.post<AuthenticationResponse>(url,authenticationRequest)
  }

  register(registerRequest: RegisterRequest):Observable<AuthenticationResponse>{
    const url=this.baseurl+"/register"
    return this.http.post<AuthenticationResponse>(url,registerRequest)
  }

  setUserToken (authenticationResponse: AuthenticationResponse){
    localStorage.setItem("accesstoken",JSON.stringify(authenticationResponse))

  }




}
