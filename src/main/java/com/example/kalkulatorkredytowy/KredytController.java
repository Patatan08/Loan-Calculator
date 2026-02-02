package com.example.kalkulatorkredytowy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class KredytController {

    @Autowired
    private KredytSerwis kredytSerwis;

    @PostMapping("/oblicz")
    public BigDecimal calculateRate(@RequestBody KredytRequest request) {
        return kredytSerwis.calculateRate(
                request.getAmount(),
                request.getMonths(),
                request.getLoanDate(),
                request.getAnnualInterestRate()

        );
    }
}



