package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Cliente;
import com.dnsouzadev.api_park.entity.ClienteVaga;
import com.dnsouzadev.api_park.entity.Vaga;
import com.dnsouzadev.api_park.util.EstacionamentoUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.findByCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.findByAvailableVaga();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.save(clienteVaga);
    }

}
