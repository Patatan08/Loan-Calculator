package com.example.kalkulatorkredytowy;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class KredytResponse {
    private BigDecimal installmentAmount;
    private BigDecimal totalInterest;
    private List<Installment> schedule;
}