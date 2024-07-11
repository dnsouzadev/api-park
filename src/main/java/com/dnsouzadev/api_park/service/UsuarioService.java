package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Usuario;
import com.dnsouzadev.api_park.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
        () -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario updatePassword(Long id, String password) {
        Usuario user = findById(id);
        user.setPassword(password);
        usuarioRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
}
