package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.ClienteVaga;
import com.dnsouzadev.api_park.exception.EntityNotFoundException;
import com.dnsouzadev.api_park.repository.ClienteVagaRepository;
import com.dnsouzadev.api_park.repository.projection.ClienteVagaProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public ClienteVaga findByRecibo(String recibo) {
        return repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException("Recibo não localizado: " + recibo)
        );
    }

    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjection> searchAllByClienteCpf(String cpf, Pageable pageable) {
        return repository.findAllByClienteCpf(cpf, pageable);
    }
}
