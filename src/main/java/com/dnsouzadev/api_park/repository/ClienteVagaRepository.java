package com.dnsouzadev.api_park.repository;

import com.dnsouzadev.api_park.entity.ClienteVaga;
import com.dnsouzadev.api_park.repository.projection.ClienteVagaProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    long countByClienteCpfAndDataSaidaIsNotNull(String cpf);

    Page<ClienteVagaProjection> findAllByClienteCpf(String cpf, Pageable pageable);
}
