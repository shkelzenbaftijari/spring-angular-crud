// ════════════════════════════════════════════════════════
// · CLIENT LIST · Table view — inline edit, delete, pagination
// ════════════════════════════════════════════════════════
import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { TooltipModule } from 'primeng/tooltip';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { MessageService, ConfirmationService } from 'primeng/api';
import { ClientService } from '../../services/client.service';
import { Client } from '../../models/client.model';

@Component({
  selector: 'app-client-list',
  imports: [
    FormsModule,
    TableModule, ButtonModule, ConfirmDialogModule,
    TooltipModule, InputTextModule, IconFieldModule, InputIconModule
  ],
  templateUrl: './client-list.html',
})
export class ClientList implements OnInit {

  private readonly clientService = inject(ClientService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly router = inject(Router);

  readonly PAGE_SIZE = 10;

  clients = signal<Client[]>([]);
  loading = signal(true);
  currentPage = signal(0);
  totalPages = signal(0);
  totalCount = signal(0);

  searchTerm = signal('');

  filteredClients = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.clients();
    return this.clients().filter(c =>
      c.referenceNumber.toLowerCase().includes(term) ||
      c.fullName.toLowerCase().includes(term) ||
      c.email.toLowerCase().includes(term) ||
      c.phone.toLowerCase().includes(term)
    );
  });

  pageNumbers = computed(() => {
    const total = this.totalPages();
    const current = this.currentPage();
    if (total <= 0) return [] as (number | null)[];
    if (total <= 7) return Array.from({ length: total }, (_, i) => i) as (number | null)[];
    const pages: (number | null)[] = [];
    if (current <= 3) {
      for (let i = 0; i < 5; i++) pages.push(i);
      pages.push(null, total - 1);
    } else if (current >= total - 4) {
      pages.push(0, null);
      for (let i = total - 5; i < total; i++) pages.push(i);
    } else {
      pages.push(0, null, current - 1, current, current + 1, null, total - 1);
    }
    return pages;
  });

  get showingRange(): string {
    const total = this.totalCount();
    if (total === 0) return 'No clients';
    const start = this.currentPage() * this.PAGE_SIZE + 1;
    const end = Math.min((this.currentPage() + 1) * this.PAGE_SIZE, total);
    return `${start}–${end} of ${total} clients`;
  }

  private editBackup: Partial<Client> = {};

  ngOnInit(): void {
    this.loadPage(0);
  }

  // · 1. LOAD ···············································

  private loadPage(page: number): void {
    this.loading.set(true);
    this.clientService.getAll(page, this.PAGE_SIZE).subscribe({
      next: (result) => {
        this.clients.set(result.data);
        this.currentPage.set(result.currentPage);
        this.totalPages.set(result.totalPages);
        this.totalCount.set(result.totalCount);
        this.loading.set(false);
      },
      error: () => {
        this.messageService.add({
          severity: 'error', summary: 'Error',
          detail: 'Could not load clients. Is the backend running?'
        });
        this.loading.set(false);
      }
    });
  }

  goToPage(page: number): void {
    if (page < 0 || page >= this.totalPages() || page === this.currentPage()) return;
    this.loadPage(page);
  }

  // · 2. INLINE EDIT ········································

  onCellEditStart(client: Client): void {
    this.editBackup = { ...client };
  }

  onCellSave(client: Client, field: keyof Client): void {
    if (client[field] === this.editBackup[field]) return;

    this.clientService.update(client.id, client).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'Saved',
          detail: `${this.fieldLabel(field)} updated.`,
          life: 2500
        });
      },
      error: (err) => {
        this.clients.update(list =>
          list.map(c => c.id === client.id ? { ...c, [field]: this.editBackup[field] } : c)
        );
        this.messageService.add({
          severity: 'error', summary: 'Error',
          detail: err.error?.message ?? 'Update failed. Change reverted.'
        });
      }
    });
  }

  private fieldLabel(field: keyof Client): string {
    const labels: Partial<Record<keyof Client, string>> = {
      referenceNumber: 'Reference number',
      fullName: 'Full name',
      email: 'Email',
      phone: 'Phone'
    };
    return labels[field] ?? field;
  }

  // · 3. NAVIGATION ·········································

  goToCreate(): void {
    this.router.navigate(['/clients/new']);
  }

  goToEdit(id: number): void {
    this.router.navigate(['/clients/edit', id]);
  }

  // · 4. DELETE ·············································

  confirmDelete(client: Client): void {
    this.confirmationService.confirm({
      message: `Delete client <strong>${client.referenceNumber}</strong> — <strong>${client.fullName}</strong>?`,
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',
      acceptButtonStyleClass: 'p-button-danger',
      accept: () => this.deleteClient(client.id)
    });
  }

  private deleteClient(id: number): void {
    this.clientService.delete(id).subscribe({
      next: () => {
        this.clients.update(list => list.filter(c => c.id !== id));
        this.messageService.add({
          severity: 'success', summary: 'Deleted',
          detail: 'Client deleted successfully.'
        });
      },
      error: (err) => {
        this.messageService.add({
          severity: 'error', summary: 'Error',
          detail: err.error?.message ?? 'Could not delete client.'
        });
      }
    });
  }
}
