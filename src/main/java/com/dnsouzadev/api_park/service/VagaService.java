package com.dnsouzadev.api_park.service;

import com.dnsouzadev.api_park.entity.Vaga;
import com.dnsouzadev.api_park.exception.CodigoUniqueViolationException;
import com.dnsouzadev.api_park.exception.EntityNotFoundException;
import com.dnsouzadev.api_park.repository.VagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dnsouzadev.api_park.entity.Vaga.StatusVaga.LIVRE;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga save(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException e) {
            throw new CodigoUniqueViolationException(String.format("code {%s} already exists.", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga findByCode(String code) {
        return vagaRepository.findByCodigo(code).orElseThrow(
        (() -> new EntityNotFoundException(String.format("Vaga with code {%s} not found.", code))));
    }

    @Transactional(readOnly = true)
    public Vaga findByAvailableVaga() {
        return vagaRepository.findFirstByStatus(LIVRE).orElseThrow(
        () -> new EntityNotFoundException("No available parking spaces."));
    }
}
