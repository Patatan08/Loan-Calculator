package com.example.kalkulatorkredytowy;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanRequest {
    @NotNull(message = "Nie podany kwoty")
    private BigDecimal loanAmount;

    @NotNull(message = "Nie podano ilości rat")
    private Integer numberOfInstallments;

    @NotNull(message = "Nie podano daty wydania")
    @FutureOrPresent(message = "Data wydania decyzji nie może być w przeszłości")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate decisionDate;

    @NotNull(message = "Wymagane roczne oprocentowanie")
    @DecimalMin(value = "0.0", message = "Oprocentowanie nie może być ujemne")
    private BigDecimal annualInterestRate;
}