package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Usuario;
import com.dnsouzadev.api_park.exception.EntityNotFoundException;
import com.dnsouzadev.api_park.exception.UsernameUniqueViolationException;
import com.dnsouzadev.api_park.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("Username {%s} already exists", usuario.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(String.format("User id=%s dont found", id)));
    }

    @Transactional
    public void updatePassword(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("new password and confirm password must be equals");
        }
        Usuario user = findById(id);

        if (!user.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("current password is invalid");
        }

        user.setPassword(novaSenha);
        usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }
}
