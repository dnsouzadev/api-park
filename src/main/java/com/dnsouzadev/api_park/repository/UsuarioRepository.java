package com.dnsouzadev.api_park.repository;

import com.dnsouzadev.api_park.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}