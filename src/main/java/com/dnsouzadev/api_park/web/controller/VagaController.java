package com.dnsouzadev.api_park.web.controller;

import com.dnsouzadev.api_park.entity.Vaga;
import com.dnsouzadev.api_park.service.VagaService;
import com.dnsouzadev.api_park.web.dto.VagaCreateDto;
import com.dnsouzadev.api_park.web.dto.VagaResponseDto;
import com.dnsouzadev.api_park.web.dto.mapper.VagaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.save(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> findByCode(@PathVariable String codigo) {
        return ResponseEntity.ok(VagaMapper.toDto(vagaService.findByCode(codigo)));
    }
}
