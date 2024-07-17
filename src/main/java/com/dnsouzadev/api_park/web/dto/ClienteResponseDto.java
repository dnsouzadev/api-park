package com.dnsouzadev.api_park.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteResponseDto implements Serializable {

    private Long id;
    private String nome;
    private String cpf;

}
