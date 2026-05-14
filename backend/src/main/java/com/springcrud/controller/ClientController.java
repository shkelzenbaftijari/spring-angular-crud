// ════════════════════════════════════════════════════════
// · CLIENT CONTROLLER · REST API — /clients
// ════════════════════════════════════════════════════════
package com.springcrud.controller;

import com.springcrud.dto.ClientDTO;
import com.springcrud.dto.PageResult;
import com.springcrud.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // · 1. CREATE ·············································

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO saved = clientService.createClient(clientDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // · 2. GET BY ID ··········································

    @GetMapping("{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok(clientService.getClientById(clientId));
    }

    // · 3. GET ALL (paginated) ·································

    @GetMapping
    public ResponseEntity<PageResult<ClientDTO>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(clientService.getPage(page, size));
    }

    // · 4. UPDATE ·············································

    @PutMapping("{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable("id") Long clientId,
                                                  @Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.updateClient(clientId, clientDTO));
    }

    // · 5. DELETE ·············································

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") Long clientId) {
        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }
}
