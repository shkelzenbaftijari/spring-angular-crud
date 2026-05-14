import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { MessageService } from 'primeng/api';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [FormsModule, InputTextModule, FloatLabelModule, ButtonModule],
  templateUrl: './login.html',
})
export class Login {

  private readonly authService = inject(AuthService);
  private readonly messageService = inject(MessageService);
  private readonly router = inject(Router);

  loginField = '';
  password = '';
  loading = signal(false);

  submit(): void {
    if (!this.loginField.trim() || !this.password.trim()) return;

    this.loading.set(true);
    this.authService.login(this.loginField.trim(), this.password).subscribe({
      next: () => this.router.navigate(['/invoices']),
      error: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Login failed',
          detail: 'Invalid username or password.',
          life: 4000
        });
        this.loading.set(false);
      }
    });
  }
}
