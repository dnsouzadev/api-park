package com.dnsouzadev.api_park.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioSenhaDto {
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters")
    private String senhaAtual;
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters")
    private String novaSenha;
    @NotBlank
    @Size(min = 6, max = 6, message = "password must be 6 characters")
    private String confirmaSenha;
}
