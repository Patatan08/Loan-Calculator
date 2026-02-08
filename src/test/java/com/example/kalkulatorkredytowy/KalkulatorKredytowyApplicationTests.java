package com.example.kalkulatorkredytowy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanServiceTest {

    @Autowired
    private KredytService kredytService;

    @ParameterizedTest
    @CsvSource({
            "3100.00, 20, 2027-03-30, 0.24, 184.59",

            "1000.00, 12, 2025-03-15, 0.20, 90.99",
            "5000.00, 12, 2025-03-15, 0.20, 454.97",
            "10000.00, 24, 2025-01-10, 0.10, 456.44",

            "5000.00, 12, 2025-03-15, 0.05, 426.24",
            "5000.00, 12, 2025-03-15, 0.25, 464.54",

            "5000.00, 12, 2025-03-15, 0.15, 445.39",
            "5000.00, 12, 2025-03-22, 0.15, 445.46",

            "10000.00, 30, 2025-01-10, 0.12, 381.51"
    })
    void Calculation(
            String kwota,
            int iloscRat,
            String dataDecyzji,
            String oprocentowanie,
            String oczekiwanaRata) {

        KredytRequest request = new KredytRequest();
        request.setLoanAmount(new BigDecimal(kwota));
        request.setNumberOfInstallments(iloscRat);
        request.setDecisionDate(LocalDate.parse(dataDecyzji));
        request.setAnnualInterestRate(new BigDecimal(oprocentowanie));

        KredytResponse response = kredytService.calculateLoanSchedule(request);

        assertNotNull(response);
        assertNotNull(response.getInstallmentAmount());

        BigDecimal expected = new BigDecimal(oczekiwanaRata);
        assertEquals(0, expected.compareTo(response.getInstallmentAmount()),
                String.format(kwota, iloscRat, dataDecyzji, oprocentowanie));
    }

    @Test
    void testInternalServerError() {
        KredytRequest request = new KredytRequest();
        request.setLoanAmount(null);
        request.setNumberOfInstallments(20);
        request.setDecisionDate(LocalDate.of(2027, 3, 30));
        request.setAnnualInterestRate(new BigDecimal("0.24"));

        Exception exception = assertThrows(Exception.class, () -> {
            kredytService.calculateLoanSchedule(request);
        });

        if (exception instanceof ResponseStatusException) {
            ResponseStatusException responseException = (ResponseStatusException) exception;
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseException.getStatusCode());
        } else {
            assertTrue(exception instanceof NullPointerException ||
                    exception instanceof IllegalArgumentException);
        }
    }
}