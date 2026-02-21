package com.loantool.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoanEligibilityGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Panels
    private HomePanel homePanel;
    private CSVProcessPanel csvProcessPanel;
    private ManualInputPanel manualInputPanel;
    private FairnessAnalysisPanel fairnessAnalysisPanel;
    private RulesConfigPanel rulesConfigPanel;
    private ReportsPanel reportsPanel;
    private SortingComparisonPanel sortingComparisonPanel;
    private SampleDataPanel sampleDataPanel;
    
    public LoanEligibilityGUI() {
        initializeFrame();
        createMenuBar();
        createMainPanel();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void initializeFrame() {
        setTitle("🏦 Loan Eligibility & Risk Tier Engine v1.0");
        setMinimumSize(new Dimension(1200, 800));
        setPreferredSize(new Dimension(1400, 900));
        
        // Set colorful theme
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            
            // Colorful UI customizations
            UIManager.put("Button.background", new Color(70, 130, 180)); // Steel blue
            UIManager.put("Button.foreground", Color.WHITE);
            UIManager.put("Button.select", new Color(100, 149, 237)); // Cornflower blue
            UIManager.put("Panel.background", new Color(245, 248, 250)); // Light gray-blue
            UIManager.put("MenuBar.background", new Color(25, 25, 112)); // Midnight blue
            UIManager.put("MenuBar.foreground", Color.WHITE);
            UIManager.put("Menu.background", new Color(25, 25, 112));
            UIManager.put("Menu.foreground", Color.WHITE);
            UIManager.put("MenuItem.background", new Color(70, 130, 180));
            UIManager.put("MenuItem.foreground", Color.WHITE);
            UIManager.put("MenuItem.select", new Color(100, 149, 237));
            UIManager.put("TextArea.background", Color.WHITE);
            UIManager.put("TextField.background", Color.WHITE);
        } catch (Exception e) {
            // Use default look and feel if customization fails
            e.printStackTrace();
        }
        
        getContentPane().setBackground(new Color(245, 248, 250));
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Navigation Menu
        JMenu navigateMenu = new JMenu("Navigate");
        
        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.addActionListener(e -> showPanel("home"));
        
        JMenuItem csvItem = new JMenuItem("Process CSV File");
        csvItem.addActionListener(e -> showPanel("csv"));
        
        JMenuItem manualItem = new JMenuItem("Manual Input");
        manualItem.addActionListener(e -> showPanel("manual"));
        
        JMenuItem fairnessItem = new JMenuItem("Fairness Analysis");
        fairnessItem.addActionListener(e -> showPanel("fairness"));
        
        JMenuItem rulesItem = new JMenuItem("Rules Configuration");
        rulesItem.addActionListener(e -> showPanel("rules"));
        
        JMenuItem reportsItem = new JMenuItem("Reports");
        reportsItem.addActionListener(e -> showPanel("reports"));
        
        JMenuItem sortingItem = new JMenuItem("Sorting Comparison");
        sortingItem.addActionListener(e -> showPanel("sorting"));
        
        JMenuItem sampleItem = new JMenuItem("Generate Sample Data");
        sampleItem.addActionListener(e -> showPanel("sample"));
        
        navigateMenu.add(homeItem);
        navigateMenu.addSeparator();
        navigateMenu.add(csvItem);
        navigateMenu.add(manualItem);
        navigateMenu.addSeparator();
        navigateMenu.add(fairnessItem);
        navigateMenu.add(rulesItem);
        navigateMenu.add(reportsItem);
        navigateMenu.addSeparator();
        navigateMenu.add(sortingItem);
        navigateMenu.add(sampleItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(navigateMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all panels
        homePanel = new HomePanel(this);
        csvProcessPanel = new CSVProcessPanel();
        manualInputPanel = new ManualInputPanel();
        fairnessAnalysisPanel = new FairnessAnalysisPanel();
        rulesConfigPanel = new RulesConfigPanel();
        reportsPanel = new ReportsPanel();
        sortingComparisonPanel = new SortingComparisonPanel();
        sampleDataPanel = new SampleDataPanel();
        
        // Add panels to card layout
        mainPanel.add(homePanel, "home");
        mainPanel.add(csvProcessPanel, "csv");
        mainPanel.add(manualInputPanel, "manual");
        mainPanel.add(fairnessAnalysisPanel, "fairness");
        mainPanel.add(rulesConfigPanel, "rules");
        mainPanel.add(reportsPanel, "reports");
        mainPanel.add(sortingComparisonPanel, "sorting");
        mainPanel.add(sampleDataPanel, "sample");
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Show home panel by default
        showPanel("home");
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    private void showAboutDialog() {
        String message = "Loan Eligibility & Risk Tier Engine v1.0\n\n" +
                "A Rule-Based Credit Decision Tool\n" +
                "Developed for Academic Project\n\n" +
                "Features:\n" +
                "• CSV File Processing\n" +
                "• Manual Applicant Entry\n" +
                "• Fairness Analysis\n" +
                "• Risk Tier Classification\n" +
                "• Sorting Algorithm Comparison\n" +
                "• Comprehensive Reporting";
        
        JOptionPane.showMessageDialog(this, message, "About", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoanEligibilityGUI().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                        "Error starting application: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}