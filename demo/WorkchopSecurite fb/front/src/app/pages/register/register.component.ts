import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { IMultiSelectOption } from 'ngx-bootstrap-multiselect';
import { RegisterRequest } from 'src/app/models/RegisterRequest';
import { Role } from 'src/app/models/role';
import { AuthService } from 'src/app/services/auth-service';
import { RoleService } from 'src/app/services/role.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
   
  registerRequest: RegisterRequest = new RegisterRequest();
  errorMsg ! : string;
  roles: Role[]=[];
  selectedRoles!: Role[];
  constructor(private authService : AuthService, private router: Router, private roleservice:RoleService) {

   }

  ngOnInit(): void {
 this.roleservice.getRoleList().subscribe({
  next:data=>{
    this.roles=data
    console.log(this.roles) 
    
  }


 })

  }
register(registerRequest :RegisterRequest){
  console.log(this.selectedRoles)
    const user ={ fullname:registerRequest.fullname,
                  email:registerRequest.email,
                password:registerRequest.password,
                 roles:this.selectedRoles}
    this.authService.register(user)
    .subscribe(result=>{
      alert("merci jamel")
      this.router.navigate(['/login'])
    console.log(result)
   
   },
    (err:HttpErrorResponse)=>this.errorMsg='this email is existe')
  }


}
