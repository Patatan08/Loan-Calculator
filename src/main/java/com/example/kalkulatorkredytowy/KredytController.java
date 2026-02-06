package com.example.kalkulatorkredytowy;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kalkulatorkredytowy")
public class KredytController {

    @Autowired
    private KredytService kredytService;

    @PostMapping("/oblicz")
    public ResponseEntity<KredytResponse> calculateLoan(@Valid @RequestBody KredytRequest request) {
        KredytResponse response = kredytService.calculateLoanSchedule(request);
        return ResponseEntity.ok(response);
    }
}



