import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { SelectModule } from 'primeng/select';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { TooltipModule } from 'primeng/tooltip';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { MessageService, ConfirmationService } from 'primeng/api';
import { UserService } from '../../services/user.service';
import { UserResponse, RegisterUserRequest } from '../../models/user.model';

@Component({
  selector: 'app-user-management',
  imports: [
    FormsModule,
    TableModule, ButtonModule, DialogModule, SelectModule,
    InputTextModule, FloatLabelModule, TooltipModule, ConfirmDialogModule
  ],
  templateUrl: './user-management.html',
})
export class UserManagement implements OnInit {

  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);

  users = signal<UserResponse[]>([]);
  loading = signal(true);
  showDialog = false;
  saving = signal(false);

  form: RegisterUserRequest = this.emptyForm();

  roleOptions = [
    { label: 'User', value: 'ROLE_USER' },
    { label: 'Admin', value: 'ROLE_ADMIN' }
  ];

  ngOnInit(): void {
    this.loadUsers();
  }

  private loadUsers(): void {
    this.loading.set(true);
    this.userService.getAll().subscribe({
      next: (data) => { this.users.set(data); this.loading.set(false); },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Could not load users.' });
        this.loading.set(false);
      }
    });
  }

  openDialog(): void {
    this.form = this.emptyForm();
    this.showDialog = true;
  }

  submit(): void {
    this.saving.set(true);
    this.userService.register(this.form).subscribe({
      next: (user) => {
        this.users.update(list => [...list, user]);
        this.messageService.add({ severity: 'success', summary: 'Created', detail: `User "${user.login}" added.`, life: 3000 });
        this.showDialog = false;
        this.saving.set(false);
      },
      error: (err) => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message ?? 'Could not create user.', life: 4000 });
        this.saving.set(false);
      }
    });
  }

  confirmDelete(user: UserResponse): void {
    this.confirmationService.confirm({
      message: `Delete user <strong>${user.login}</strong>?`,
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.deleteUser(user)
    });
  }

  private deleteUser(user: UserResponse): void {
    this.userService.delete(user.id).subscribe({
      next: () => {
        this.users.update(list => list.filter(u => u.id !== user.id));
        this.messageService.add({ severity: 'success', summary: 'Deleted', detail: `User "${user.login}" deleted.` });
      },
      error: () => {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Could not delete user.' });
      }
    });
  }

  get isFormValid(): boolean {
    return !!(this.form.login.trim() && this.form.email.trim() &&
              this.form.password.trim() && this.form.role);
  }

  private emptyForm(): RegisterUserRequest {
    return { login: '', email: '', password: '', firstName: '', lastName: '', role: 'ROLE_USER' };
  }
}
