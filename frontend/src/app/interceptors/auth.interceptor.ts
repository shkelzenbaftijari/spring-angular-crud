import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // Skip adding token for login endpoint
  if (req.url.includes('/api/auth/')) {
    return next(req);
  }

  const token = authService.getToken();
  const cloned = token
    ? req.clone({ headers: req.headers.set('Authorization', `Bearer ${token}`) })
    : req;

  return next(cloned).pipe(
    catchError((err: HttpErrorResponse) => {
      // Token expired or invalid — force logout
      if (err.status === 401) {
        authService.logout();
      }
      return throwError(() => err);
    })
  );
};
