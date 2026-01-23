package com.example.kalkulatorfinansowy;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KredytRequest {
    private BigDecimal amount;
    private int months;
    private LocalDate loanDate;
    private BigDecimal annualInterestRate;
}