package com.loantool.ui;

import com.loantool.algorithms.EligibilityEngine;
import com.loantool.algorithms.MergeSorter;
import com.loantool.algorithms.RiskClassifier;
import com.loantool.models.Applicant;
import com.loantool.models.LoanDecision;
import com.loantool.utils.FileHandler;
import com.loantool.utils.ProcessedDataContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Vector;

public class CSVProcessPanel extends JPanel {
    private JTextField filePathField;
    private JButton browseButton;
    private JButton processButton;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JTable resultsTable;
    private JLabel summaryLabel;

    public CSVProcessPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(245, 248, 250));
        createPanel();
    }

    private void createPanel() {
        // Top Panel - File Selection
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(220, 237, 253)); // Light blue background
        TitledBorder topBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(25, 118, 210), 2),
                "CSV File Selection");
        topBorder.setTitleColor(new Color(25, 118, 210));
        topBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        topPanel.setBorder(topBorder);

        JPanel filePanel = new JPanel(new BorderLayout(5, 5));
        filePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        filePathField = new JTextField("data/input/applicants.csv");
        filePathField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        browseButton = new JButton("Browse...");
        browseButton.setBackground(new Color(70, 130, 180));
        browseButton.setForeground(Color.WHITE);
        browseButton.setFocusPainted(false);
        browseButton.addActionListener(e -> browseForFile());

        processButton = new JButton("Process CSV File");
        processButton.setFont(new Font("Arial", Font.BOLD, 14));
        processButton.setBackground(new Color(46, 125, 50)); // Green
        processButton.setForeground(Color.WHITE);
        processButton.setFocusPainted(false);
        processButton.addActionListener(e -> processCSV());

        filePanel.add(new JLabel("File Path:"), BorderLayout.WEST);
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);

        topPanel.add(filePanel, BorderLayout.CENTER);
        topPanel.add(processButton, BorderLayout.SOUTH);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");

        // Center Panel - Results Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 245, 238)); // Light peach
        TitledBorder centerBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(237, 108, 2), 2),
                "Results");
        centerBorder.setTitleColor(new Color(237, 108, 2));
        centerBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        centerPanel.setBorder(centerBorder);

        String[] columnNames = {"ID", "Income", "Debt", "Credit Score",
                "Employment", "Loan Requested", "Risk Score", "Risk Tier",
                "Approved", "Recommended Limit", "Interest Rate"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setPreferredSize(new Dimension(1100, 300));

        centerPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Summary Label
        summaryLabel = new JLabel("No data processed yet");
        summaryLabel.setFont(new Font("Arial", Font.BOLD, 12));
        summaryLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        centerPanel.add(summaryLabel, BorderLayout.SOUTH);

        // Bottom Panel - Log Area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(243, 229, 245)); // Light purple
        TitledBorder bottomBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(123, 31, 162), 2),
                "Processing Log");
        bottomBorder.setTitleColor(new Color(123, 31, 162));
        bottomBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.setBorder(bottomBorder);

        logArea = new JTextArea(8, 50);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        logArea.setEditable(false);
        logArea.setBackground(Color.WHITE);
        logArea.setForeground(new Color(30, 30, 30));

        JScrollPane logScrollPane = new JScrollPane(logArea);
        bottomPanel.add(logScrollPane, BorderLayout.CENTER);

        // Add components
        add(topPanel, BorderLayout.NORTH);
        add(progressBar, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle("Select CSV File");
        fileChooser.setCurrentDirectory(new File("data/input"));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void processCSV() {
        processButton.setEnabled(false);
        browseButton.setEnabled(false);
        logArea.setText("");
        progressBar.setString("Processing...");
        progressBar.setIndeterminate(true);

        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            private List<LoanDecision> decisions;

            @Override
            protected Void doInBackground() throws Exception {
                String filePath = filePathField.getText().trim();

                publish("Loading applicants from: " + filePath);
                List<Applicant> applicants = FileHandler.loadApplicantsFromCSV(filePath);
                publish("Loaded " + applicants.size() + " applicants");

                if (applicants.isEmpty()) {
                    publish("ERROR: No valid applicants found in the file.");
                    return null;
                }

                // Step 1: Eligibility Check
                publish("\nEvaluating eligibility...");
                EligibilityEngine engine = new EligibilityEngine();
                List<Applicant> eligibleApplicants = engine.evaluateEligibility(applicants);
                publish("Eligible: " + eligibleApplicants.size() +
                        " | Rejected: " + (applicants.size() - eligibleApplicants.size()));

                if (eligibleApplicants.isEmpty()) {
                    publish("ERROR: No eligible applicants found!");
                    return null;
                }

                // Step 2: Calculate Risk Scores
                publish("\nCalculating risk scores...");
                for (Applicant applicant : eligibleApplicants) {
                    applicant.calculateRiskScore();
                }
                publish("Risk scores calculated");

                // Step 3: Sort by Risk Score (using Merge Sort by default)
                publish("\nSorting applicants...");
                MergeSorter mergeSorter = new MergeSorter();
                long startTime = System.currentTimeMillis();
                List<Applicant> sortedApplicants = mergeSorter.sort(eligibleApplicants);
                long sortTime = System.currentTimeMillis() - startTime;
                publish("Sorted using Merge Sort in " + sortTime + " ms");

                // Step 4: Classify Risk Tiers
                publish("\nClassifying risk tiers...");
                RiskClassifier classifier = new RiskClassifier();
                decisions = classifier.classify(sortedApplicants);
                publish("Classification completed");

                // Step 5: Save Results
                publish("\nSaving results...");
                FileHandler.saveDecisionsToJSON(decisions, "data/output/results.json");
                FileHandler.saveDecisionsToCSV(decisions, "data/output/results.csv");
                publish("Results saved to data/output/");

                // Step 6: Build BST and Linked List (data structures)
                publish("\nBuilding data structures (BST, Linked List)...");
                ProcessedDataContext.getInstance().setProcessedData(sortedApplicants, decisions);
                publish("BST: " + ProcessedDataContext.getInstance().getApplicantBST().size() + " nodes | " +
                        "Linked List: " + ProcessedDataContext.getInstance().getDecisionLinkedList().size() + " decisions");

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    logArea.append(message + "\n");
                }
                logArea.setCaretPosition(logArea.getDocument().getLength());
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setString("Complete");
                processButton.setEnabled(true);
                browseButton.setEnabled(true);

                try {
                    if (decisions != null && !decisions.isEmpty()) {
                        displayResults(decisions);
                        JOptionPane.showMessageDialog(CSVProcessPanel.this,
                                "Processing completed successfully!\n" +
                                        "Results saved to data/output/",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CSVProcessPanel.this,
                            "Error processing file: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    logArea.append("\nERROR: " + e.getMessage() + "\n");
                }
            }
        };

        worker.execute();
    }

    private void displayResults(List<LoanDecision> decisions) {
        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
        model.setRowCount(0);

        int approved = 0;
        int lowRisk = 0, mediumRisk = 0, highRisk = 0;

        for (LoanDecision decision : decisions) {
            Applicant app = decision.getApplicant();

            Vector<Object> row = new Vector<>();
            row.add(app.getId());
            row.add(String.format("$%,.2f", app.getMonthlyIncome()));
            row.add(String.format("$%,.2f", app.getExistingDebt()));
            row.add(app.getCreditScore());
            row.add(app.getEmploymentDuration() + " months");
            row.add(String.format("$%,.2f", app.getLoanAmountRequested()));
            row.add(String.format("%.1f", app.getRiskScore()));
            row.add(decision.getRiskTier().getDisplayName());
            row.add(decision.isApproved() ? "Yes" : "No");
            row.add(String.format("$%,.2f", decision.getRecommendedLimit()));
            row.add(String.format("%.1f%%", decision.getInterestRate() * 100));

            model.addRow(row);

            if (decision.isApproved()) approved++;

            switch (decision.getRiskTier()) {
                case LOW_RISK: lowRisk++; break;
                case MEDIUM_RISK: mediumRisk++; break;
                case HIGH_RISK: highRisk++; break;
            }
        }

        String summary = String.format(
                "Total: %d | Approved: %d (%.1f%%) | Low Risk: %d | Medium Risk: %d | High Risk: %d",
                decisions.size(), approved, (approved * 100.0) / decisions.size(),
                lowRisk, mediumRisk, highRisk);
        summaryLabel.setText(summary);
    }
}