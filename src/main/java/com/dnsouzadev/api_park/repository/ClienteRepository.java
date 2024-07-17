package com.dnsouzadev.api_park.repository;

import com.dnsouzadev.api_park.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
