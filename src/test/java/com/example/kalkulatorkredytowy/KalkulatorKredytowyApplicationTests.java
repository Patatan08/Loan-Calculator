package com.example.kalkulatorkredytowy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class KalkulatorKredytowyApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testValidRequest() {
        KredytRequest request = new KredytRequest(
                new BigDecimal("10000"),
                12,
                LocalDate.now(),
                new BigDecimal("5.0")
        );

        HttpEntity<KredytRequest> httpEntity = new HttpEntity<>(request);

        ResponseEntity<BigDecimal> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/oblicz",
                httpEntity,
                BigDecimal.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("875.00"), response.getBody());
    }

    @Test
    void testWhenAmountIsNull() {
        KredytRequest request = new KredytRequest(
                null,
                12,
                LocalDate.now(),
                new BigDecimal("5.0")
        );
        HttpEntity<KredytRequest> httpEntity = new HttpEntity<>(request);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/oblicz",
                httpEntity,
                String.class
        );
        System.out.println(response.getStatusCode());
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
    }
}