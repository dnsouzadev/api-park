package com.dnsouzadev.api_park.web.dto.mapper;

import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.web.dto.ClienteCreateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteCreateDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteCreateDto.class);
    }

}
