package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.exception.CpfUniqueViolationException;
import com.dnsouzadev.api_park.exception.EntityNotFoundException;
import com.dnsouzadev.api_park.repository.ClienteRepository;
import com.dnsouzadev.api_park.repository.projection.ClienteProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente save(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new CpfUniqueViolationException(String.format("CPF {%s} already exists", cliente.getCpf()));
        }

    }

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                (() -> new EntityNotFoundException(String.format("Client with id {%d} not found", id))));
    }

    @Transactional
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> getAll(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

}