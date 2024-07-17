package com.dnsouzadev.api_park.web.dto.mapper;

import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.web.dto.ClienteCreateDto;
import com.dnsouzadev.api_park.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

}
