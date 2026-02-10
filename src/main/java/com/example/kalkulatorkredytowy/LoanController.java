package com.example.kalkulatorkredytowy;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/calculate")
    public ResponseEntity<LoanResponse> calculateLoan(@Valid @RequestBody LoanRequest request) {
        LoanResponse response = loanService.calculateLoanSchedule(request);
        return ResponseEntity.ok(response);
    }
}