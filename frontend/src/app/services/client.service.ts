// ════════════════════════════════════════════════════════
// · CLIENT SERVICE · HttpClient CRUD calls to /clients
// ════════════════════════════════════════════════════════
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, CreateClientRequest, PageResult } from '../models/client.model';

@Injectable({ providedIn: 'root' })
export class ClientService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/clients';

  getAll(page = 0, size = 10): Observable<PageResult<Client>> {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<PageResult<Client>>(this.apiUrl, { params });
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  create(client: CreateClientRequest): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  update(id: number, client: CreateClientRequest): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
