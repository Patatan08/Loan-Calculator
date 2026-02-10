package com.example.kalkulatorkredytowy;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private static final int DAYS_IN_YEAR = 365;
    private static final int ROUNDING_SCALE = 2;

    public LoanResponse calculateLoanSchedule(LoanRequest request) {
        BigDecimal loanAmount = request.getLoanAmount();
        Integer numberOfInstallments = request.getNumberOfInstallments();
        LocalDate decisionDate = request.getDecisionDate();
        BigDecimal annualInterestRate = request.getAnnualInterestRate();

        BigDecimal technicalCapitalInstallment = loanAmount.divide(
                BigDecimal.valueOf(numberOfInstallments),
                ROUNDING_SCALE,
                RoundingMode.HALF_UP
        );
        List<Installment> schedule = generateSchedule(
                loanAmount,
                numberOfInstallments,
                decisionDate,
                annualInterestRate,
                technicalCapitalInstallment
        );

        BigDecimal totalInterest = calculateTotalInterest(schedule);
        BigDecimal installmentAmount = calculateInstallmentAmount(loanAmount, totalInterest, numberOfInstallments);

        return new LoanResponse(installmentAmount, totalInterest, schedule);
    }

    private List<Installment> generateSchedule(
            BigDecimal loanAmount,
            Integer numberOfInstallments,
            LocalDate decisionDate,
            BigDecimal annualInterestRate,
            BigDecimal technicalCapitalInstallment) {

        List<Installment> schedule = new ArrayList<>();

        schedule.add(new Installment(
                0,
                0,
                decisionDate,
                loanAmount,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));

        LocalDate firstPaymentDate = calculateFirstPaymentDate(decisionDate);

        BigDecimal remainingCapital1 = loanAmount.subtract(technicalCapitalInstallment);
        schedule.add(new Installment(
                1,
                0,
                firstPaymentDate,
                remainingCapital1,
                technicalCapitalInstallment,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));

        LocalDate previousDate = firstPaymentDate;
        BigDecimal previousRemainingCapital = remainingCapital1;

        for (int i = 2; i <= numberOfInstallments; i++) {
            LocalDate currentPaymentDate = previousDate.plusMonths(1);
            int daysForPayment = (int) ChronoUnit.DAYS.between(previousDate, currentPaymentDate);
            BigDecimal remainingCapital = previousRemainingCapital.subtract(technicalCapitalInstallment);
            BigDecimal dailyInterest = calculateDailyInterest(
                    previousRemainingCapital,
                    annualInterestRate,
                    daysForPayment
            );

            schedule.add(new Installment(
                    i,
                    daysForPayment,
                    currentPaymentDate,
                    remainingCapital,
                    technicalCapitalInstallment,
                    dailyInterest,
                    BigDecimal.ZERO
            ));
            previousDate = currentPaymentDate;
            previousRemainingCapital = remainingCapital;
        }

        return schedule;
    }

    private LocalDate calculateFirstPaymentDate(LocalDate decisionDate) {
        int dayOfMonth = decisionDate.getDayOfMonth();
        if (dayOfMonth >= 21) {
            return decisionDate.plusMonths(2).withDayOfMonth(10);
        } else {
            return decisionDate.plusMonths(1).withDayOfMonth(10);
        }
    }

    private BigDecimal calculateDailyInterest(
            BigDecimal remainingCapital,
            BigDecimal annualInterestRate,
            int daysForPayment) {
        BigDecimal dailyRate = annualInterestRate.divide(
                BigDecimal.valueOf(DAYS_IN_YEAR),
                10,
                RoundingMode.HALF_UP
        );

        BigDecimal interest = remainingCapital
                .multiply(dailyRate)
                .multiply(BigDecimal.valueOf(daysForPayment))
                .setScale(ROUNDING_SCALE, RoundingMode.HALF_UP);

        return interest;
    }

    private BigDecimal calculateTotalInterest(List<Installment> schedule) {
        return schedule.stream()
                .skip(2)
                .map(Installment::getDailyInterest)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(ROUNDING_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateInstallmentAmount(
            BigDecimal loanAmount,
            BigDecimal totalInterest,
            Integer numberOfInstallments) {
        BigDecimal totalAmount = loanAmount.add(totalInterest);
        BigDecimal installmentAmount = totalAmount.divide(
                BigDecimal.valueOf(numberOfInstallments),
                ROUNDING_SCALE,
                RoundingMode.HALF_UP
        );

        return installmentAmount;
    }
}