package com.example.kalkulatorkredytowy;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class KredytSerwis {

    public BigDecimal calculateRate(BigDecimal amount, int months,
                                    LocalDate loanDate, BigDecimal annualInterestRate) {

        LocalDate firstPaymentDate = loanDate.withDayOfMonth(1).plusMonths(1);

        long daysToFirstPayment = ChronoUnit.DAYS.between(loanDate, firstPaymentDate);

        BigDecimal dailyInterestRate = annualInterestRate
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .divide(new BigDecimal("365"), 10, RoundingMode.HALF_UP);

        BigDecimal interestForFirstPeriod = amount
                .multiply(dailyInterestRate)
                .multiply(new BigDecimal(daysToFirstPayment));

        BigDecimal monthlyPayment = calculateAnnuityPayment(amount, months, annualInterestRate);

        BigDecimal firstPaymentWithInterest = monthlyPayment.add(interestForFirstPeriod);

        return monthlyPayment.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAnnuityPayment(BigDecimal amount, int months, BigDecimal annualInterestRate) {
        BigDecimal monthlyInterestRate = annualInterestRate
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP)
                .divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyInterestRate);
        BigDecimal powOnePlusR = onePlusR.pow(months);

        BigDecimal numerator = monthlyInterestRate.multiply(powOnePlusR);
        BigDecimal denominator = powOnePlusR.subtract(BigDecimal.ONE);

        BigDecimal annuityFactor = numerator.divide(denominator, 10, RoundingMode.HALF_UP);

        return amount.multiply(annuityFactor);
    }
}