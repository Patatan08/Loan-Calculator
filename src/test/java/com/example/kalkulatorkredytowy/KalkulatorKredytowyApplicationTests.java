package com.example.kalkulatorkredytowy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KalkulatorKredytowyApplicationTests {

    @Autowired
    private KredytService kredytService;

    private KredytRequest kredytRequest;

    @BeforeEach
    void setUp() {
        kredytRequest = new KredytRequest();
        kredytRequest.setLoanAmount(new BigDecimal("3100.00"));
        kredytRequest.setNumberOfInstallments(20);
        kredytRequest.setDecisionDate(LocalDate.of(2027, 3, 30));
        kredytRequest.setAnnualInterestRate(new BigDecimal("0.24"));
    }

    @Test
    void Calculation() {
        BigDecimal expectedInstallmentAmount = new BigDecimal("184.59");
        KredytResponse response = kredytService.calculateLoanSchedule(kredytRequest);
        assertNotNull(response);
        assertNotNull(response.getInstallmentAmount());
        assertEquals(0, expectedInstallmentAmount.compareTo(response.getInstallmentAmount()));
    }
    @Test
    void INTERNAL_SERVER_ERROR_HANDLE() {
        kredytRequest.setLoanAmount(null);
        Exception exception = assertThrows(Exception.class, () -> {
            kredytService.calculateLoanSchedule(kredytRequest);});
        if (exception instanceof ResponseStatusException) {
            ResponseStatusException responseException = (ResponseStatusException) exception;
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseException.getStatusCode());
        } else {
            assertTrue(exception instanceof NullPointerException || exception instanceof IllegalArgumentException);
        }
    }
}