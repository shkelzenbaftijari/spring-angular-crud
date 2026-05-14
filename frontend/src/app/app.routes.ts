// ════════════════════════════════════════════════════════
// · APP ROUTES · Client-side routing configuration
// ════════════════════════════════════════════════════════
import { Routes } from '@angular/router';
import { Shell } from './components/shell/shell';
import { ClientList } from './components/client-list/client-list';
import { ClientForm } from './components/client-form/client-form';
import { Login } from './components/login/login';
import { UserManagement } from './components/user-management/user-management';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Shell,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'clients', pathMatch: 'full' },
      { path: 'clients', component: ClientList },
      { path: 'clients/new', component: ClientForm },
      { path: 'clients/edit/:id', component: ClientForm },
      { path: 'users', component: UserManagement },
    ]
  },
];
