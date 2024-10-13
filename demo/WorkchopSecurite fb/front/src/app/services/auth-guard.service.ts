import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { AuthService } from './auth-service';

@Injectable({
  providedIn: 'root'
})
 export class AuthGuardService implements CanActivate {

  constructor(private authservice : AuthService) { }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
   return this.authservice.isUserAuthenticated();
}
 }