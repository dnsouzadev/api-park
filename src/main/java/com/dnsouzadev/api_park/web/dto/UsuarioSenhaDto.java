package com.dnsouzadev.api_park.web.dto;

import lombok.*;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioSenhaDto {

    private String senhaAtual;
    private String novaSenha;
    private String confirmaSenha;
}
