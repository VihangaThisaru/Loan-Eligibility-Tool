package com.loantool.algorithms;

import com.loantool.models.Applicant;
import com.loantool.models.LoanDecision;
import com.loantool.models.RiskTier;
import com.loantool.config.RulesConfig;

import java.util.ArrayList;
import java.util.List;

public class RiskClassifier {
    private final RulesConfig config;

    public RiskClassifier() {
        this.config = RulesConfig.getInstance();
    }

    public LoanDecision classifySingle(Applicant applicant) {
        double score = applicant.getRiskScore();
        RiskTier tier = binarySearchRiskTier(score);
        boolean approved = tier != RiskTier.HIGH_RISK || score >= 40;

        String reason = String.format("Risk Score: %.1f → %s", score, tier.getDisplayName());

        return new LoanDecision(applicant, tier, approved, reason);
    }

    public List<LoanDecision> classify(List<Applicant> applicants) {
        List<LoanDecision> decisions = new ArrayList<>();

        for (Applicant applicant : applicants) {
            decisions.add(classifySingle(applicant));
        }

        return decisions;
    }

    

    


}