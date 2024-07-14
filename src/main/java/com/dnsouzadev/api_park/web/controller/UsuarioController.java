package com.dnsouzadev.api_park.web.controller;

import com.dnsouzadev.api_park.entity.Usuario;
import com.dnsouzadev.api_park.service.UsuarioService;
import com.dnsouzadev.api_park.web.dto.UsuarioCreateDto;
import com.dnsouzadev.api_park.web.dto.UsuarioResponseDto;
import com.dnsouzadev.api_park.web.dto.UsuarioSenhaDto;
import com.dnsouzadev.api_park.web.dto.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> findAll() {
        List<Usuario> users = usuarioService.findAll();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id) {
        Usuario user = usuarioService.findById(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.save(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
        usuarioService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

}
