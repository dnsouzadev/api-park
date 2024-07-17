package com.dnsouzadev.api_park.web.controller;

import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.jwt.JwtUserDetails;
import com.dnsouzadev.api_park.service.ClienteService;
import com.dnsouzadev.api_park.service.UsuarioService;
import com.dnsouzadev.api_park.web.dto.ClienteCreateDto;
import com.dnsouzadev.api_park.web.dto.ClienteResponseDto;
import com.dnsouzadev.api_park.web.dto.UsuarioResponseDto;
import com.dnsouzadev.api_park.web.dto.mapper.ClienteMapper;
import com.dnsouzadev.api_park.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Clients", description = "Client management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Create a new client", responses = {
            @ApiResponse(responseCode = "201", description = "Client created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClienteResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Client already exists",
                    content = @Content(mediaType = "application/json",
                            schema=@Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema=@Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.findById(userDetails.getId()));
        clienteService.save(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));

    }


}
