import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/utils/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ViewCertsGuard implements CanActivate {
  constructor(private _authService: AuthService, private router: Router) { }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if(localStorage.getItem('token') == null){
      this.router.navigate(['/login']);
      return false;  
    }  
    return new Promise(async (resolve, reject) => {
      this._authService.getPermissions().toPromise().then((response) => {
        var permissions = response
        console.log(permissions)
        var hasReadCertPermission: boolean = false;
        for (var i in permissions) {
          var j = +i;
          if (permissions[j] === 'read_certificate') {
            hasReadCertPermission = true;
          }
        }
        console.log(hasReadCertPermission)
        if (hasReadCertPermission) {
          console.log("in if")
          resolve(true);
          return true;
        }
        else {
          this.router.navigate(['/']);
          resolve(false);
          return false;
        }
      })
    })

  }
}