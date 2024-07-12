package com.dnsouzadev.api_park.web.dto;

import lombok.*;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class UsuarioResponseDto {

    private Long id;
    private String username;
    private String role;

}
