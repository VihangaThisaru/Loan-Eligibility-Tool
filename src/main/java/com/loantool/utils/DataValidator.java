package com.loantool.utils;

import com.loantool.models.Applicant;

import java.util.regex.Pattern;

public class DataValidator {

    // Validation patterns
    private static final Pattern ID_PATTERN = Pattern.compile("^APP\\d{3,6}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z ]{2,50}$");

    // Validation constants
    private static final double MIN_INCOME = 0.0;
    private static final double MAX_INCOME = 1000000.0;
    private static final double MIN_DEBT = 0.0;
    private static final double MAX_DEBT = 500000.0;
    private static final int MIN_CREDIT_SCORE = 300;
    private static final int MAX_CREDIT_SCORE = 850;
    private static final int MIN_EMPLOYMENT_MONTHS = 0;
    private static final int MAX_EMPLOYMENT_MONTHS = 600; // 50 years
    private static final double MIN_LOAN_AMOUNT = 100.0;
    private static final double MAX_LOAN_AMOUNT = 1000000.0;

    public static ValidationResult validateApplicant(Applicant applicant) {
        ValidationResult result = new ValidationResult();

        // Validate ID
        if (!isValidId(applicant.getId())) {
            result.addError("ID", "Invalid ID format. Expected: APP followed by 3-6 digits");
        }

        // Validate income
        if (!isValidIncome(applicant.getMonthlyIncome())) {
            result.addError("Monthly Income",
                    String.format("Income must be between $%.2f and $%.2f",
                            MIN_INCOME, MAX_INCOME));
        }

        // Validate debt
        if (!isValidDebt(applicant.getExistingDebt())) {
            result.addError("Existing Debt",
                    String.format("Debt must be between $%.2f and $%.2f",
                            MIN_DEBT, MAX_DEBT));
        }

        // Validate credit score
        if (!isValidCreditScore(applicant.getCreditScore())) {
            result.addError("Credit Score",
                    String.format("Credit score must be between %d and %d",
                            MIN_CREDIT_SCORE, MAX_CREDIT_SCORE));
        }

        // Validate employment duration
        if (!isValidEmploymentDuration(applicant.getEmploymentDuration())) {
            result.addError("Employment Duration",
                    String.format("Employment must be between %d and %d months",
                            MIN_EMPLOYMENT_MONTHS, MAX_EMPLOYMENT_MONTHS));
        }

        // Validate loan amount
        if (!isValidLoanAmount(applicant.getLoanAmountRequested())) {
            result.addError("Loan Amount",
                    String.format("Loan amount must be between $%.2f and $%.2f",
                            MIN_LOAN_AMOUNT, MAX_LOAN_AMOUNT));
        }

        // Business logic validations
        if (applicant.getExistingDebt() > applicant.getMonthlyIncome() * 100) {
            result.addError("Debt-to-Income",
                    "Debt appears unreasonably high compared to income");
        }

        if (applicant.getLoanAmountRequested() < 100 &&
                applicant.getMonthlyIncome() > 10000) {
            result.addWarning("Loan Amount",
                    "Loan amount seems unusually low for income level");
        }

        return result;
    }

    public static ValidationResult validateCSVRow(String[] row, int lineNumber) {
        ValidationResult result = new ValidationResult();

        if (row.length < 6) {
            result.addError("CSV Format",
                    String.format("Line %d: Expected 6 columns, found %d",
                            lineNumber, row.length));
            return result;
        }

        try {
            // Validate ID
            String id = row[0].trim();
            if (!isValidId(id)) {
                result.addError("ID",
                        String.format("Line %d: Invalid ID format: %s", lineNumber, id));
            }

            // Validate numeric fields
            double income = Double.parseDouble(row[1].trim());
            if (!isValidIncome(income)) {
                result.addError("Monthly Income",
                        String.format("Line %d: Invalid income: %s", lineNumber, row[1]));
            }

            double debt = Double.parseDouble(row[2].trim());
            if (!isValidDebt(debt)) {
                result.addError("Existing Debt",
                        String.format("Line %d: Invalid debt: %s", lineNumber, row[2]));
            }

            int creditScore = Integer.parseInt(row[3].trim());
            if (!isValidCreditScore(creditScore)) {
                result.addError("Credit Score",
                        String.format("Line %d: Invalid credit score: %s", lineNumber, row[3]));
            }

            int employmentMonths = Integer.parseInt(row[4].trim());
            if (!isValidEmploymentDuration(employmentMonths)) {
                result.addError("Employment Duration",
                        String.format("Line %d: Invalid employment months: %s",
                                lineNumber, row[4]));
            }

            double loanAmount = Double.parseDouble(row[5].trim());
            if (!isValidLoanAmount(loanAmount)) {
                result.addError("Loan Amount",
                        String.format("Line %d: Invalid loan amount: %s", lineNumber, row[5]));
            }

        } catch (NumberFormatException e) {
            result.addError("Numeric Format",
                    String.format("Line %d: Invalid numeric value", lineNumber));
        }

        return result;
    }

    // Individual validation methods
    public static boolean isValidId(String id) {
        return id != null && ID_PATTERN.matcher(id).matches();
    }

    public static boolean isValidIncome(double income) {
        return income >= MIN_INCOME && income <= MAX_INCOME;
    }

    public static boolean isValidDebt(double debt) {
        return debt >= MIN_DEBT && debt <= MAX_DEBT;
    }

    public static boolean isValidCreditScore(int score) {
        return score >= MIN_CREDIT_SCORE && score <= MAX_CREDIT_SCORE;
    }

    public static boolean isValidEmploymentDuration(int months) {
        return months >= MIN_EMPLOYMENT_MONTHS && months <= MAX_EMPLOYMENT_MONTHS;
    }

    public static boolean isValidLoanAmount(double amount) {
        return amount >= MIN_LOAN_AMOUNT && amount <= MAX_LOAN_AMOUNT;
    }

    public static boolean isValidDebtToIncomeRatio(double debt, double income) {
        return income > 0 && (debt / income) <= 10.0; // Reasonable upper bound
    }

    // Inner class for validation results
    public static class ValidationResult {
        private final StringBuilder errors;
        private final StringBuilder warnings;
        private boolean isValid;

        public ValidationResult() {
            this.errors = new StringBuilder();
            this.warnings = new StringBuilder();
            this.isValid = true;
        }

        public void addError(String field, String message) {
            errors.append("❌ ").append(field).append(": ").append(message).append("\n");
            isValid = false;
        }

        public void addWarning(String field, String message) {
            warnings.append("⚠️ ").append(field).append(": ").append(message).append("\n");
        }

        public boolean isValid() {
            return isValid;
        }

        public boolean hasWarnings() {
            return warnings.length() > 0;
        }

        public String getErrorMessages() {
            return errors.toString();
        }

        public String getWarningMessages() {
            return warnings.toString();
        }

        public String getAllMessages() {
            return errors.toString() + warnings.toString();
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (!isValid) {
                result.append("VALIDATION FAILED:\n");
                result.append(errors.toString());
            }
            if (hasWarnings()) {
                result.append("WARNINGS:\n");
                result.append(warnings.toString());
            }
            if (isValid && !hasWarnings()) {
                result.append("All validations passed");
            }
            return result.toString();
        }
    }
}