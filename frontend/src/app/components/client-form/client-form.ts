// ════════════════════════════════════════════════════════
// · CLIENT FORM · Create / full edit page
// ════════════════════════════════════════════════════════
import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
import { MessageService } from 'primeng/api';
import { ClientService } from '../../services/client.service';
import { CreateClientRequest } from '../../models/client.model';

@Component({
  selector: 'app-client-form',
  imports: [FormsModule, CardModule, InputTextModule, FloatLabelModule, ButtonModule, DividerModule],
  templateUrl: './client-form.html',
})
export class ClientForm implements OnInit {

  private readonly clientService = inject(ClientService);
  private readonly messageService = inject(MessageService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  isEditMode = signal(false);
  editId = signal<number | null>(null);
  saving = signal(false);
  ready = signal(false);

  form: CreateClientRequest = {
    referenceNumber: '',
    fullName: '',
    email: '',
    phone: ''
  };

  // · 1. INIT ···············································

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode.set(true);
      this.editId.set(+id);
      this.clientService.getById(+id).subscribe({
        next: (client) => {
          this.form.referenceNumber = client.referenceNumber;
          this.form.fullName = client.fullName;
          this.form.email = client.email;
          this.form.phone = client.phone;
          this.ready.set(true);
        },
        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Client not found.'
          });
        }
      });
    } else {
      this.ready.set(true);
    }
  }

  // · 2. SUBMIT ·············································

  submit(): void {
    this.saving.set(true);
    const id = this.editId();

    const request$ = id
      ? this.clientService.update(id, this.form)
      : this.clientService.create(this.form);

    request$.subscribe({
      next: () => this.router.navigate(['/clients']),
      error: (err) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: err.error?.message ?? 'Save failed. Please try again.'
        });
        this.saving.set(false);
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/clients']);
  }

  get isFormValid(): boolean {
    return !!(
      this.form.referenceNumber?.trim() &&
      this.form.fullName?.trim() &&
      this.form.email?.trim() &&
      this.form.phone?.trim()
    );
  }
}
