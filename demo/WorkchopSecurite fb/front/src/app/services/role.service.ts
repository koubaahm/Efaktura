import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Role } from '../models/role';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  baseurl = 'http://localhost:8080/role'
 
  constructor(private http :HttpClient) {

   }
   getallrole():Observable<Role[]>{
    
    return  this.http.get<Role[]>(this.baseurl+ "/getallroles")
   }


   getRoleList(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.baseurl}/getallroles`)
    .pipe(
      map((response:any) => response as Role[])
    );
    
  }
}
