// ════════════════════════════════════════════════════════
// · CLIENT SERVICE · Business logic — CRUD + pagination
// ════════════════════════════════════════════════════════
package com.springcrud.service;

import com.springcrud.dto.ClientDTO;
import com.springcrud.dto.PageResult;
import com.springcrud.entity.Client;
import com.springcrud.mapper.ClientMapper;
import com.springcrud.repository.ClientRepository;
import com.springcrud.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepo;

    @Autowired
    ClientService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    // · 1. GET ALL ············································

    public List<ClientDTO> getAllClients() {
        return clientRepo.findAllByOrderByIdDesc()
                .stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
    }

    // · 2. PAGINATION ·········································

    public PageResult<ClientDTO> getPage(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        var result = clientRepo.findAll(pageable);
        List<ClientDTO> dtos = result.getContent().stream().map(ClientMapper::toDto).collect(Collectors.toList());
        return new PageResult<>(dtos, result.getTotalPages(), page, result.getTotalElements());
    }

    // · 3. GET BY ID ··········································

    public ClientDTO getClientById(Long clientId) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> {
                    LOG.warn("Client id={} not found", clientId);
                    return new ResourceNotFoundException("Client not found: " + clientId);
                });
        return ClientMapper.toDto(client);
    }

    // · 4. CREATE ·············································

    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = ClientMapper.toEntity(clientDTO);
        Client saved = clientRepo.save(client);
        LOG.info("Client created with id={}", saved.getId());
        return ClientMapper.toDto(saved);
    }

    // · 5. UPDATE ·············································

    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> {
                    LOG.warn("Cannot update — Client id={} not found", clientId);
                    return new ResourceNotFoundException("Client not found: " + clientId);
                });
        client.setReferenceNumber(clientDTO.getReferenceNumber());
        client.setFullName(clientDTO.getFullName());
        client.setEmail(clientDTO.getEmail());
        client.setPhone(clientDTO.getPhone());
        Client updated = clientRepo.save(client);
        LOG.info("Client id={} updated", updated.getId());
        return ClientMapper.toDto(updated);
    }

    // · 6. DELETE ·············································

    public void deleteClient(Long clientId) {
        clientRepo.findById(clientId)
                .orElseThrow(() -> {
                    LOG.warn("Cannot delete — Client id={} not found", clientId);
                    return new ResourceNotFoundException("Client not found: " + clientId);
                });
        clientRepo.deleteById(clientId);
        LOG.info("Client id={} deleted", clientId);
    }
}
