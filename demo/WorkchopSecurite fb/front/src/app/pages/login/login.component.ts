import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { AuthService } from 'src/app/services/auth-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  authenticationRequest :AuthenticationRequest=new AuthenticationRequest();
  errorMsg : string=""
  path: string = "../assets/img/boxed-bg.jpg";
  
  
    constructor(private authService : AuthService, private router: Router) { }
  
    ngOnInit(): void {
      localStorage.removeItem("accesstoken")
    }
    login(){
      this.authService.login(this.authenticationRequest).subscribe(
        ress =>{
          this.authService.setUserToken(ress)
          
          this.router.navigate(["/register"])

      },error =>{
        this.errorMsg= "login ou mot de pass incorect"
      }
    )

  }

}
