import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/utils/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class NewCertGuard implements CanActivate {
  constructor(private _authService: AuthService, private router: Router) { }
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return new Promise(async (resolve, reject) => {
      this._authService.getPermissions().toPromise().then((response) => {
        var permissions = response
        console.log(permissions)
        var hasCreateCertPermission: boolean = false;
        for (var i in permissions) {
          var j = +i;
          if (permissions[j] === 'create_ee_certificate' || permissions[j] === 'create_inter_certificate' || permissions[j] === 'create_root_certificate') {
            hasCreateCertPermission = true;
          }
        }
        console.log(hasCreateCertPermission)
        if (hasCreateCertPermission) {
          resolve(true);
          return true;
        }
        else {
          this.router.navigate(['/overview']);
          resolve(false);
          return false;
        }
      })
    })
  }

}
