package com.loantool.ui;

import com.loantool.algorithms.EligibilityEngine;
import com.loantool.algorithms.RiskClassifier;
import com.loantool.models.Applicant;
import com.loantool.models.LoanDecision;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ManualInputPanel extends JPanel {
    private JTextField idField;
    private JTextField incomeField;
    private JTextField debtField;
    private JTextField creditScoreField;
    private JTextField employmentField;
    private JTextField loanAmountField;
    private JButton evaluateButton;
    private JTextArea resultsArea;

    public ManualInputPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 248, 250));
        createPanel();
    }

    private void createPanel() {
        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(220, 237, 253)); // Light blue
        TitledBorder inputBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
                "Applicant Information");
        inputBorder.setTitleColor(new Color(25, 118, 210));
        inputBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.setBorder(inputBorder);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Applicant ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Applicant ID:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(20);
        idField.setText("APP001");
        inputPanel.add(idField, gbc);

        // Monthly Income
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Monthly Income ($):"), gbc);
        gbc.gridx = 1;
        incomeField = new JTextField(20);
        inputPanel.add(incomeField, gbc);

        // Existing Debt
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Existing Debt ($):"), gbc);
        gbc.gridx = 1;
        debtField = new JTextField(20);
        inputPanel.add(debtField, gbc);

        // Credit Score
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Credit Score (300-850):"), gbc);
        gbc.gridx = 1;
        creditScoreField = new JTextField(20);
        inputPanel.add(creditScoreField, gbc);

        // Employment Duration
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("Employment Duration (months):"), gbc);
        gbc.gridx = 1;
        employmentField = new JTextField(20);
        inputPanel.add(employmentField, gbc);

        // Loan Amount Requested
        gbc.gridx = 0;
        gbc.gridy = 5;
        inputPanel.add(new JLabel("Loan Amount Requested ($):"), gbc);
        gbc.gridx = 1;
        loanAmountField = new JTextField(20);
        inputPanel.add(loanAmountField, gbc);

        // Evaluate Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        evaluateButton = new JButton("Evaluate Applicant");
        evaluateButton.setFont(new Font("Arial", Font.BOLD, 14));
        evaluateButton.setBackground(new Color(46, 125, 50)); // Green
        evaluateButton.setForeground(Color.WHITE);
        evaluateButton.setFocusPainted(false);
        evaluateButton.addActionListener(e -> evaluateApplicant());
        inputPanel.add(evaluateButton, gbc);

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBackground(new Color(255, 245, 238)); // Light peach
        TitledBorder resultsBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(237, 108, 2), 2),
                "Evaluation Results");
        resultsBorder.setTitleColor(new Color(237, 108, 2));
        resultsBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        resultsPanel.setBorder(resultsBorder);

        resultsArea = new JTextArea(15, 50);
        resultsArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        resultsArea.setEditable(false);
        resultsArea.setBackground(Color.WHITE);
        resultsArea.setForeground(new Color(30, 30, 30));
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(inputPanel, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.WEST);
        add(resultsPanel, BorderLayout.CENTER);
    }

    private void evaluateApplicant() {
        try {
            String id = idField.getText().trim();
            double income = Double.parseDouble(incomeField.getText().trim());
            double debt = Double.parseDouble(debtField.getText().trim());
            int creditScore = Integer.parseInt(creditScoreField.getText().trim());
            int employment = Integer.parseInt(employmentField.getText().trim());
            double loanAmount = Double.parseDouble(loanAmountField.getText().trim());

            Applicant applicant = new Applicant(id, income, debt, creditScore, employment, loanAmount);
            EligibilityEngine engine = new EligibilityEngine();
            RiskClassifier classifier = new RiskClassifier();

            resultsArea.setText("");
            resultsArea.append("════════════════════════════════════════════════════════\n");
            resultsArea.append("                 EVALUATION RESULTS\n");
            resultsArea.append("════════════════════════════════════════════════════════\n\n");

            if (engine.isEligible(applicant)) {
                applicant.calculateRiskScore();
                LoanDecision decision = classifier.classifySingle(applicant);

                resultsArea.append("✅ ELIGIBLE FOR LOAN\n");
                resultsArea.append("────────────────────────────────────────────────────────\n");
                resultsArea.append(String.format("Applicant ID:      %s\n", applicant.getId()));
                resultsArea.append(String.format("Risk Score:        %.1f/100\n", applicant.getRiskScore()));
                resultsArea.append(String.format("Risk Tier:         %s\n", decision.getRiskTier().getDisplayName()));
                resultsArea.append(String.format("Interest Rate:     %.1f%%\n",
                        decision.getRiskTier().getBaseInterestRate() * 100));
                resultsArea.append(String.format("Requested Amount:  $%,.2f\n", applicant.getLoanAmountRequested()));
                resultsArea.append(String.format("Recommended Limit: $%,.2f\n", decision.getRecommendedLimit()));
                resultsArea.append(String.format("Decision:          %s\n", decision.getDecisionReason()));

                if (decision.isApproved()) {
                    resultsArea.append("\n🎉 Congratulations! Loan application is APPROVED!\n");
                } else {
                    resultsArea.append("\n⚠️  Application requires further review.\n");
                }
            } else {
                resultsArea.append("❌ NOT ELIGIBLE FOR LOAN\n");
                resultsArea.append("────────────────────────────────────────────────────────\n");
                resultsArea.append("Rejection Reason: " + engine.getRejectionReason(applicant) + "\n");
                resultsArea.append("\n💡 Suggestions:\n");
                resultsArea.append("1. Improve credit score\n");
                resultsArea.append("2. Reduce existing debt\n");
                resultsArea.append("3. Maintain employment for longer duration\n");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for all fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error evaluating applicant: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
