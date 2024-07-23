package com.dnsouzadev.api_park;

import com.dnsouzadev.api_park.web.dto.EstacionamentoCreateDto;
import io.jsonwebtoken.Jwt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCheckIn_withValidData_ShouldReturn201() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("AAA0A00")
                .marca("Chevrolet")
                .modelo("Onix")
                .cor("Preto")
                .clienteCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("api/v1/estacionamentos/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("AAA0A00")
                .jsonPath("marca").isEqualTo("Chevrolet")
                .jsonPath("modelo").isEqualTo("Onix")
                .jsonPath("cor").isEqualTo("Preto")
                .jsonPath("clienteCpf").isEqualTo("09191773016")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("vagaCodigo").exists();
    }

    @Test
    public void createCheckIn_withValidDataAndRoleClient_ShouldReturn403() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("AAA0A00")
                .marca("Chevrolet")
                .modelo("Onix")
                .cor("Preto")
                .clienteCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("api/v1/estacionamentos/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_withInvalidData_ShouldReturn422() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("")
                .marca("")
                .modelo("")
                .cor("")
                .clienteCpf("")
                .build();

        testClient
                .post()
                .uri("api/v1/estacionamentos/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_withCpfDoesNotExists_ShouldReturn404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("AAA0A00")
                .marca("Chevrolet")
                .modelo("Onix")
                .cor("Preto")
                .clienteCpf("68895535006")
                .build();

        testClient
                .post()
                .uri("api/v1/estacionamentos/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamento-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamento-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckIn_withNoONeFreeParkingVaga_ShouldReturn404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("AAA0A00")
                .marca("Chevrolet")
                .modelo("Onix")
                .cor("Preto")
                .clienteCpf("09191773016")
                .build();

        testClient
                .post()
                .uri("api/v1/estacionamentos/check-in")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void findCheckIn_withAdminRole_ShouldReturn200() {
        testClient
                .get()
                .uri("api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_withClientRole_ShouldReturn200() {
        testClient
                .get()
                .uri("api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com.br", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void findCheckIn_withNonexistentRecibo_ShouldReturn404() {
        testClient
                .get()
                .uri("api/v1/estacionamentos/check-in/{recibo}", "20230313-999999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("method").isEqualTo("GET");
    }
}
