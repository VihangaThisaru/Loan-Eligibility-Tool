package com.loantool.ui;

import com.loantool.datastructures.ApplicantBST;
import com.loantool.datastructures.DecisionLinkedList;
import com.loantool.models.Applicant;
import com.loantool.models.LoanDecision;
import com.loantool.models.RiskTier;
import com.loantool.utils.FileHandler;
import com.loantool.utils.ProcessedDataContext;
import com.loantool.utils.ReportGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private JButton loadButton;
    private JButton generateReportButton;
    private JButton bstQueryButton;
    private JButton linkedListAuditButton;
    private JTextArea reportArea;
    private JLabel summaryLabel;
    
    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 248, 250));
        createPanel();
    }
    
    private void createPanel() {
        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setBackground(new Color(220, 237, 253)); // Light blue
        TitledBorder controlBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
            "Report Actions");
        controlBorder.setTitleColor(new Color(25, 118, 210));
        controlBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        controlPanel.setBorder(controlBorder);
        
        loadButton = new JButton("Load Results");
        loadButton.setBackground(new Color(70, 130, 180)); // Steel blue
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.addActionListener(e -> loadResults());
        
        generateReportButton = new JButton("Generate Detailed Report");
        generateReportButton.setBackground(new Color(237, 108, 2)); // Orange
        generateReportButton.setForeground(Color.WHITE);
        generateReportButton.setFocusPainted(false);
        generateReportButton.addActionListener(e -> generateReport());
        
        bstQueryButton = new JButton("BST Range Query");
        bstQueryButton.setBackground(new Color(104, 159, 56)); // Green
        bstQueryButton.setForeground(Color.WHITE);
        bstQueryButton.setFocusPainted(false);
        bstQueryButton.setToolTipText("Find applicants in risk score range (uses BST)");
        bstQueryButton.addActionListener(e -> showBSTQuery());

        linkedListAuditButton = new JButton("Linked List Audit");
        linkedListAuditButton.setBackground(new Color(123, 31, 162)); // Purple
        linkedListAuditButton.setForeground(Color.WHITE);
        linkedListAuditButton.setFocusPainted(false);
        linkedListAuditButton.setToolTipText("View decision audit trail (Linked List traversal)");
        linkedListAuditButton.addActionListener(e -> showLinkedListAudit());

        controlPanel.add(loadButton);
        controlPanel.add(generateReportButton);
        controlPanel.add(bstQueryButton);
        controlPanel.add(linkedListAuditButton);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(new Color(255, 245, 238)); // Light peach
        TitledBorder summaryBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(237, 108, 2), 2),
            "Summary Statistics");
        summaryBorder.setTitleColor(new Color(237, 108, 2));
        summaryBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        summaryPanel.setBorder(summaryBorder);
        
        summaryLabel = new JLabel("No results loaded");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        summaryLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        summaryPanel.add(summaryLabel, BorderLayout.CENTER);
        
        // Report Display Area
        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(new Color(243, 229, 245)); // Light purple
        TitledBorder reportBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(123, 31, 162), 2),
            "Detailed Report");
        reportBorder.setTitleColor(new Color(123, 31, 162));
        reportBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        reportPanel.setBorder(reportBorder);
        
        reportArea = new JTextArea(20, 70);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        reportArea.setEditable(false);
        reportArea.setBackground(Color.WHITE);
        reportArea.setForeground(new Color(30, 30, 30));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        reportPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Layout
        add(controlPanel, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.CENTER);
        add(reportPanel, BorderLayout.SOUTH);
    }
    
    private void loadResults() {
        try {
            List<LoanDecision> decisions = FileHandler.loadDecisionsFromJSON("data/output/results.json");
            
            if (decisions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No results found. Please process a CSV file first.",
                        "No Data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ProcessedDataContext.getInstance().setFromLoadedDecisions(decisions);
            displaySummary(decisions);
            displayDetailedReport(decisions);
            
            JOptionPane.showMessageDialog(this,
                    "Results loaded successfully!\nBST and Linked List built for queries.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading results: " + e.getMessage() + "\n\n" +
                    "Make sure you have processed a CSV file first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateReport() {
        try {
            List<LoanDecision> decisions = FileHandler.loadDecisionsFromJSON("data/output/results.json");
            
            if (decisions.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No results found. Please process a CSV file first.",
                        "No Data",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            ReportGenerator.generateReport(decisions, "data/output/detailed_report.txt");
            
            displaySummary(decisions);
            displayDetailedReport(decisions);
            
            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\n" +
                    "Saved to: data/output/detailed_report.txt",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error generating report: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displaySummary(List<LoanDecision> decisions) {
        int lowRisk = 0, mediumRisk = 0, highRisk = 0;
        int approved = 0;
        double totalRequested = 0, totalRecommended = 0;
        
        for (LoanDecision decision : decisions) {
            RiskTier tier = decision.getRiskTier();
            if (tier == RiskTier.LOW_RISK) {
                lowRisk++;
            } else if (tier == RiskTier.MEDIUM_RISK) {
                mediumRisk++;
            } else {
                highRisk++;
            }
            
            if (decision.isApproved()) {
                approved++;
            }
            
            totalRequested += decision.getApplicant().getLoanAmountRequested();
            totalRecommended += decision.getRecommendedLimit();
        }
        
        String summary = String.format(
                "<html><div style='padding:10px;'>" +
                "<b>Total Applicants:</b> %d<br>" +
                "<b>Approved:</b> %d (%.1f%%)<br>" +
                "<b>Total Amount Requested:</b> $%,.2f<br>" +
                "<b>Total Recommended Limit:</b> $%,.2f<br><br>" +
                "<b>Risk Distribution:</b><br>" +
                "Low Risk: %d (%.1f%%) | Medium Risk: %d (%.1f%%) | High Risk: %d (%.1f%%)" +
                "</div></html>",
                decisions.size(),
                approved, (approved * 100.0) / decisions.size(),
                totalRequested, totalRecommended,
                lowRisk, (lowRisk * 100.0) / decisions.size(),
                mediumRisk, (mediumRisk * 100.0) / decisions.size(),
                highRisk, (highRisk * 100.0) / decisions.size()
        );
        
        summaryLabel.setText(summary);
    }
    
    private void displayDetailedReport(List<LoanDecision> decisions) {
        StringBuilder report = new StringBuilder();
        
        report.append("════════════════════════════════════════════════════════\n");
        report.append("                 DETAILED REPORT\n");
        report.append("════════════════════════════════════════════════════════\n\n");
        
        int index = 1;
        for (LoanDecision decision : decisions) {
            report.append(String.format("Applicant #%d: %s\n", index++, decision.getApplicant().getId()));
            report.append("────────────────────────────────────────────────────────\n");
            report.append(decision.getDetailedExplanation());
            report.append("\n\n");
        }
        
        reportArea.setText(report.toString());
    }

    private void showBSTQuery() {
        ApplicantBST bst = ProcessedDataContext.getInstance().getApplicantBST();
        if (bst == null || bst.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No data loaded. Load results or process a CSV file first.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String minStr = JOptionPane.showInputDialog(this, "Enter minimum risk score:", "50");
        if (minStr == null) return;
        String maxStr = JOptionPane.showInputDialog(this, "Enter maximum risk score:", "80");
        if (maxStr == null) return;

        try {
            double minScore = Double.parseDouble(minStr);
            double maxScore = Double.parseDouble(maxStr);
            java.util.List<Applicant> inRange = bst.findInRange(minScore, maxScore);

            StringBuilder sb = new StringBuilder();
            sb.append("BST Range Query: Risk Score [").append(minScore).append(" - ").append(maxScore).append("]\n");
            sb.append("========================================\n");
            sb.append("Found ").append(inRange.size()).append(" applicant(s)\n\n");
            for (Applicant a : inRange) {
                sb.append(String.format("%s | Score: %.1f | Income: $%,.0f | Credit: %d\n",
                        a.getId(), a.getRiskScore(), a.getMonthlyIncome(), a.getCreditScore()));
            }
            reportArea.setText(sb.toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLinkedListAudit() {
        DecisionLinkedList list = ProcessedDataContext.getInstance().getDecisionLinkedList();
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No data loaded. Load results or process a CSV file first.",
                    "No Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        reportArea.setText(list.getAuditTrail());
    }
}