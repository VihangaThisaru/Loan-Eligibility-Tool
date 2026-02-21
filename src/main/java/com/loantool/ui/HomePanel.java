package com.loantool.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {
    private LoanEligibilityGUI parent;

    public HomePanel(LoanEligibilityGUI parent) {
        this.parent = parent;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        createPanel();
    }

    private void createPanel() {
        setBackground(new Color(245, 248, 250));

        // Header with gradient effect
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 25, 112)); // Midnight blue
        headerPanel.setBorder(new EmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel("🏦 Loan Eligibility & Risk Tier Engine", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel subtitleLabel = new JLabel("A Rule-Based Credit Decision Tool", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(173, 216, 230)); // Light blue
        subtitleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Button Panel with colorful background
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        buttonPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        buttonPanel.setBackground(new Color(245, 248, 250));

        // Create buttons with different colors
        JButton csvButton = createMenuButton("📊 Process CSV File",
                "Load and process applicant data from CSV file");
        csvButton.setBackground(new Color(46, 125, 50)); // Green
        csvButton.addActionListener(e -> parent.showPanel("csv"));

        JButton manualButton = createMenuButton("📝 Manual Input",
                "Evaluate a single applicant manually");
        manualButton.setBackground(new Color(25, 118, 210)); // Blue
        manualButton.addActionListener(e -> parent.showPanel("manual"));

        JButton fairnessButton = createMenuButton("🔍 Fairness Analysis",
                "Analyze fairness vs risk trade-offs");
        fairnessButton.setBackground(new Color(123, 31, 162)); // Purple
        fairnessButton.addActionListener(e -> parent.showPanel("fairness"));

        JButton rulesButton = createMenuButton("⚙️ Rules Configuration",
                "View and update eligibility rules");
        rulesButton.setBackground(new Color(211, 47, 47)); // Red
        rulesButton.addActionListener(e -> parent.showPanel("rules"));

        JButton reportsButton = createMenuButton("📄 Generate Reports",
                "View detailed reports and statistics");
        reportsButton.setBackground(new Color(237, 108, 2)); // Orange
        reportsButton.addActionListener(e -> parent.showPanel("reports"));

        JButton sortingButton = createMenuButton("⚡ Sorting Comparison",
                "Compare sorting algorithm performance");
        sortingButton.setBackground(new Color(0, 151, 167)); // Cyan
        sortingButton.addActionListener(e -> parent.showPanel("sorting"));

        JButton sampleButton = createMenuButton("📋 Generate Sample Data",
                "Create sample CSV files for testing");
        sampleButton.setBackground(new Color(104, 159, 56)); // Light green
        sampleButton.addActionListener(e -> parent.showPanel("sample"));

        JButton exitButton = createMenuButton("🚪 Exit",
                "Exit the application");
        exitButton.setBackground(new Color(97, 97, 97)); // Gray
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(csvButton);
        buttonPanel.add(manualButton);
        buttonPanel.add(fairnessButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(reportsButton);
        buttonPanel.add(sortingButton);
        buttonPanel.add(sampleButton);
        buttonPanel.add(exitButton);

        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(300, 90));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Colorful button styling
        button.setBackground(new Color(70, 130, 180)); // Steel blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createRaisedBevelBorder(),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Cornflower blue
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180)); // Steel blue
            }
        });

        return button;
    }
}
