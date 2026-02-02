package com.example.kalkulatorkredytowy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KalkulatorFinansowyApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void Calculation() {
        KredytRequest request = new KredytRequest(
                new BigDecimal("3100"),
                18,
                LocalDate.of(2025, 9, 19),
                new BigDecimal("23.0")
        );

        ResponseEntity<BigDecimal> response = restTemplate.postForEntity(
                "/oblicz",
                new HttpEntity<>(request),
                BigDecimal.class
        );

        assertEquals(200, response.getStatusCode().value());
        BigDecimal rata = response.getBody();

        System.out.println(rata);
        System.out.println("200.29 zł");

        BigDecimal expected = new BigDecimal("200.29");
        BigDecimal roznica = rata.subtract(expected).abs();
        System.out.println(roznica);
    }

    @Test
    void testWhenAmountIsNull() {
        KredytRequest request = new KredytRequest(
                null,
                18,
                LocalDate.now(),
                new BigDecimal("23.0")
        );

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/oblicz",
                new HttpEntity<>(request),
                String.class
        );
        assertTrue(response.getStatusCode().is5xxServerError());
    }
}