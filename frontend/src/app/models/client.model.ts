// ════════════════════════════════════════════════════════
// · CLIENT MODEL · TypeScript interfaces for client data
// ════════════════════════════════════════════════════════

export interface Client {
  id: number;
  referenceNumber: string;
  fullName: string;
  email: string;
  phone: string;
}

export interface CreateClientRequest {
  referenceNumber: string;
  fullName: string;
  email: string;
  phone: string;
}

export interface PageResult<T> {
  data: T[];
  totalPages: number;
  currentPage: number;
  totalCount: number;
}
