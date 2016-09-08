package com.oozmakappa.oyeloans.Models;

/**
 * Created by sankarnarayanan on 08/09/16.
 */
public class LoanSummaryModel {

    private String loanId = "";
    private  String loanStatus = "";

    public void setLoanId(String loanId){
        this.loanId = loanId;
    }

    public void setLoanStatus(String loanStatus){
        this.loanStatus = loanStatus;
    }

    public String getLoanId() {
        return loanId;
    }

    public String getLoanStatus() {
        return loanStatus;
    }
}