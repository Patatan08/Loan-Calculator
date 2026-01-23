package com.example.kalkulatorkredytowy;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class KredytSerwis {

    public BigDecimal calculateRate(BigDecimal sum, BigDecimal LoanInterest, int months) {
        if(sum == null){
            throw new RuntimeException("Podana kwota jest błędna");
        }
        if(LoanInterest == null){
            throw new RuntimeException("Podane oprocentowanie jest błędne");
        }
        if (sum.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Kwota musi być większa od 0");
        }
        if(months <= 0){
            throw new RuntimeException("Podana ilość miesięcy jest błędna");
        }
        BigDecimal years = new BigDecimal(months).divide(new BigDecimal(12), 10, BigDecimal.ROUND_HALF_UP);

            BigDecimal interest = sum
                    .multiply(LoanInterest)
                    .divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP)
                    .multiply(years);

        BigDecimal total = sum.add(interest);

        BigDecimal installment = total.divide(new BigDecimal(months), 2, BigDecimal.ROUND_HALF_UP);

        return installment;
    }
}