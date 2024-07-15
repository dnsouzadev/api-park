package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Usuario;
import com.dnsouzadev.api_park.exception.EntityNotFoundException;
import com.dnsouzadev.api_park.exception.PasswordInvalidException;
import com.dnsouzadev.api_park.exception.UsernameUniqueViolationException;
import com.dnsouzadev.api_park.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
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
            throw new PasswordInvalidException("New password and confirm password are different");
        }

        Usuario user = findById(id);

        if (!passwordEncoder.matches(senhaAtual, user.getPassword())) {
            throw new PasswordInvalidException("Current password is invalid");
        }

        user.setPassword(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElseThrow(
        () -> new EntityNotFoundException(String.format("User username=%s dont found", username)));
    }

    @Transactional(readOnly = true)
    public Usuario.Role getRoleByUsername(String username) {
        return usuarioRepository.findRoleByUsername(username).orElseThrow(
        () -> new EntityNotFoundException(String.format("Role from username=%s dont found", username)));
    }
}
