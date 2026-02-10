package com.example.kalkulatorkredytowy;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Installment {
    private Integer installmentNumber;
    private Integer daysForPayment;
    private LocalDate paymentDate;
    private BigDecimal remainingCapital;
    private BigDecimal technicalCapitalInstallment;
    private BigDecimal dailyInterest;
    private BigDecimal installmentAmount;
}