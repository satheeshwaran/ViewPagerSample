package com.oozmakappa.oyeloans.Models;

/**
 * Created by sricharan.c on 18-09-2016.
 */
public class Application {
    public String applicationID = "";
    public String loanAmount = "";
    public String loanDuration = "";
    public LoanUser loanUserObject;
    public BankInfo bankInfoObject;
    public String firstPayDate = "";

    public String loanReason = "";
    public String preferredApplicationPickupDate = "";
    public String preferredApplicationPickupTime = "";

    // Here we must supply the page name to track the application state.
    public String applicationState = "";

}
