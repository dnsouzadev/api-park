package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.ClienteVaga;
import com.dnsouzadev.api_park.repository.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository repository;

    @Transactional
    public ClienteVaga save(ClienteVaga clienteVaga) {
        return repository.save(clienteVaga);
    }

}
