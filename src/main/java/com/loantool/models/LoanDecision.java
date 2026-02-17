package com.loantool.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoanDecision {
    private Applicant applicant;
    private RiskTier riskTier;
    private boolean approved;
    private String decisionReason;
    private LocalDateTime decisionTime;
    private double recommendedLimit;
    private double interestRate;

    // Constructor 1: With all parameters
    public LoanDecision(Applicant applicant, RiskTier riskTier,
            boolean approved, String decisionReason) {
        this.applicant = applicant;
        this.riskTier = riskTier;
        this.approved = approved;
        this.decisionReason = decisionReason;
        this.decisionTime = LocalDateTime.now();
        this.interestRate = riskTier.getBaseInterestRate();
        this.recommendedLimit = calculateRecommendedLimit();
    }

    // Constructor 2: With interest rate parameter
    public LoanDecision(Applicant applicant, RiskTier riskTier,
            boolean approved, String decisionReason, double interestRate) {
        this.applicant = applicant;
        this.riskTier = riskTier;
        this.approved = approved;
        this.decisionReason = decisionReason;
        this.decisionTime = LocalDateTime.now();
        this.interestRate = interestRate;
        this.recommendedLimit = calculateRecommendedLimit();
    }

    private double calculateRecommendedLimit() {
        if (!approved)
            return 0;

        double base = applicant.getMonthlyIncome() * 3; // 3 months income
        double multiplier;

        // Java 11 compatible switch statement
        if (riskTier == RiskTier.LOW_RISK) {
            multiplier = 1.5;
        } else if (riskTier == RiskTier.MEDIUM_RISK) {
            multiplier = 1.0;
        } else { // HIGH_RISK
            multiplier = 0.5;
        }

        double calculatedLimit = base * multiplier;
        double maxAllowed = applicant.getLoanAmountRequested() * 1.2;

        return Math.min(calculatedLimit, maxAllowed);
    }

    // Getters
    public Applicant getApplicant() {
        return applicant;
    }

    public RiskTier getRiskTier() {
        return riskTier;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getDecisionReason() {
        return decisionReason;
    }

    public LocalDateTime getDecisionTime() {
        return decisionTime;
    }

    public double getRecommendedLimit() {
        return recommendedLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // Formatted getters
    public String getFormattedDecisionTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return decisionTime.format(formatter);
    }

    public String getFormattedRecommendedLimit() {
        return String.format("$%,.2f", recommendedLimit);
    }

    public String getFormattedInterestRate() {
        return String.format("%.1f%%", interestRate * 100);
    }

    public String getFormattedMonthlyPayment(int loanTermMonths) {
        if (!approved || recommendedLimit <= 0 || loanTermMonths <= 0) {
            return "$0.00";
        }

        double monthlyRate = interestRate / 12;
        double monthlyPayment = (recommendedLimit * monthlyRate * Math.pow(1 + monthlyRate, loanTermMonths)) /
                (Math.pow(1 + monthlyRate, loanTermMonths) - 1);

        return String.format("$%,.2f", monthlyPayment);
    }

    // Setters
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
        this.recommendedLimit = calculateRecommendedLimit();
    }

    public void setRiskTier(RiskTier riskTier) {
        this.riskTier = riskTier;
        this.interestRate = riskTier.getBaseInterestRate();
        this.recommendedLimit = calculateRecommendedLimit();
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
        this.recommendedLimit = calculateRecommendedLimit();
    }

    public void setDecisionReason(String decisionReason) {
        this.decisionReason = decisionReason;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    // Business logic methods
    public boolean isHighRisk() {
        return riskTier == RiskTier.HIGH_RISK;
    }

    public boolean isLowRisk() {
        return riskTier == RiskTier.LOW_RISK;
    }

    public boolean isMediumRisk() {
        return riskTier == RiskTier.MEDIUM_RISK;
    }

    public double getDebtToIncomeRatio() {
        return applicant.getDebtToIncomeRatio();
    }

    public double getRequestedToRecommendedRatio() {
        if (applicant.getLoanAmountRequested() == 0)
            return 0;
        return recommendedLimit / applicant.getLoanAmountRequested();
    }

    public String getRiskColorCode() {
        switch (riskTier) {
            case LOW_RISK:
                return "🟢"; // Green
            case MEDIUM_RISK:
                return "🟡"; // Yellow
            case HIGH_RISK:
                return "🔴"; // Red
            default:
                return "⚪"; // White
        }
    }

    public String getApprovalStatusSymbol() {
        return approved ? "✅" : "❌";
    }

    // func to get detailed explanation of the decision
    public String getDetailedExplanation() {
        StringBuilder explanation = new StringBuilder();

        explanation.append("Loan Decision Analysis:\n");
        explanation.append("======================\n");
        explanation.append("Applicant ID: ").append(applicant.getId()).append("\n");
        explanation.append("Risk Tier: ").append(riskTier.getDisplayName()).append(" ").append(getRiskColorCode())
                .append("\n");
        explanation.append("Risk Score: ").append(String.format("%.1f", applicant.getRiskScore())).append("/100\n");
        explanation.append("Approval Status: ").append(approved ? "APPROVED" : "REJECTED").append(" ")
                .append(getApprovalStatusSymbol()).append("\n");

        if (approved) {
            explanation.append("Recommended Limit: ").append(getFormattedRecommendedLimit()).append("\n");
            explanation.append("Interest Rate: ").append(getFormattedInterestRate()).append("\n");
            explanation.append("Monthly Payment (36 months): ").append(getFormattedMonthlyPayment(36)).append("\n");
        }

        explanation.append("Decision Reason: ").append(decisionReason).append("\n");
        explanation.append("Decision Time: ").append(getFormattedDecisionTime()).append("\n");

        return explanation.toString();
    }
}