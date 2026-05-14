// ════════════════════════════════════════════════════════
// · CLIENT MAPPER · Static mapping — entity ↔ DTO
// ════════════════════════════════════════════════════════
package com.springcrud.mapper;

import com.springcrud.dto.ClientDTO;
import com.springcrud.entity.Client;

public class ClientMapper {

    // toEntity — no id, JPA generates it on insert
    public static Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setReferenceNumber(dto.getReferenceNumber());
        client.setFullName(dto.getFullName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        return client;
    }

    // toDto — includes id so the client knows which record it is
    public static ClientDTO toDto(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setReferenceNumber(client.getReferenceNumber());
        dto.setFullName(client.getFullName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        return dto;
    }
}
