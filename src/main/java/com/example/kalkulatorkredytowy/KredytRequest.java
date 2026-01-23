package com.example.kalkulatorkredytowy;
import java.math.BigDecimal;
import java.time.LocalDate;

public class KredytRequest {
   private  BigDecimal amount;
   private int months;
   private LocalDate loanDate;
   private BigDecimal annualInterestRate;

   public KredytRequest(){}

  public BigDecimal getAmount(){

       return amount;
  }
  public void setAmount(BigDecimal amount ){

       this.amount = amount;
  }
  public int getMonths(){

       return months;
  }
  public void setMonths(int months) {

       this.months = months;
  }
  public LocalDate getLoanDate(){

       return loanDate;
  }
  public void setLoanDate(LocalDate loanDate){
      this.loanDate = loanDate;
  }
  public BigDecimal getAnnualInterestRate(){

       return annualInterestRate;
  }
  public void setAnnualInterestRate(BigDecimal annualInterestRate){

       this.annualInterestRate = annualInterestRate;
    }




}
