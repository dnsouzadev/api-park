package com.dnsouzadev.api_park.web.controller;

import com.dnsouzadev.api_park.entity.Usuario;
import com.dnsouzadev.api_park.service.UsuarioService;
import com.dnsouzadev.api_park.web.dto.UsuarioCreateDto;
import com.dnsouzadev.api_park.web.dto.UsuarioResponseDto;
import com.dnsouzadev.api_park.web.dto.UsuarioSenhaDto;
import com.dnsouzadev.api_park.web.dto.mapper.UsuarioMapper;
import com.dnsouzadev.api_park.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "List all users", description = "request required a bearer token, only admin can access",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "list of all users",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseDto>> findAll() {
        List<Usuario> users = usuarioService.findAll();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

    @Operation(summary = "get user by id", description = "request required a bearer token",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id) ")
    public ResponseEntity<UsuarioResponseDto> findById(@PathVariable Long id) {
        Usuario user = usuarioService.findById(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "User already exists",
                    content = @Content(mediaType = "application/json",
                            schema=@Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema=@Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.save(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "update password", description = "request required a bearer token, only user can access",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "password uopdated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "400", description = "password does not match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Validation error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@Valid @PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {
        usuarioService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

}
