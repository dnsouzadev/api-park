package com.dnsouzadev.api_park.web.controller;

import ch.qos.logback.core.net.server.Client;
import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.jwt.JwtUserDetails;
import com.dnsouzadev.api_park.repository.projection.ClienteProjection;
import com.dnsouzadev.api_park.service.ClienteService;
import com.dnsouzadev.api_park.service.UsuarioService;
import com.dnsouzadev.api_park.web.dto.ClienteCreateDto;
import com.dnsouzadev.api_park.web.dto.ClienteResponseDto;
import com.dnsouzadev.api_park.web.dto.PageableDto;
import com.dnsouzadev.api_park.web.dto.UsuarioResponseDto;
import com.dnsouzadev.api_park.web.dto.mapper.ClienteMapper;
import com.dnsouzadev.api_park.web.dto.mapper.PageableMapper;
import com.dnsouzadev.api_park.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients", description = "Client management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Create a new client",
            security = @SecurityRequirement(name = "security"),
            responses = {
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

    @Operation(summary = "get client by id", description = "request required a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.findById(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "List all clients", description = "request required a bearer token, only admin can access",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representing the page number"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representing total elements per page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id, asc")),
                            description = "Representing the sort order"
                    ),
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "list of all clients",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ClienteResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.getAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @Operation(summary = "get details by client", description = "request required a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.searchByUserId(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }


}
