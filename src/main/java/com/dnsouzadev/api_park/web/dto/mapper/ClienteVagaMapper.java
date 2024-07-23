package com.dnsouzadev.api_park.web.dto.mapper;

import com.dnsouzadev.api_park.entity.ClienteVaga;
import com.dnsouzadev.api_park.web.dto.EstacionamentoCreateDto;
import com.dnsouzadev.api_park.web.dto.EstacionamentoResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga entity) {
        return new ModelMapper().map(entity, EstacionamentoResponseDto.class);
    }
}
