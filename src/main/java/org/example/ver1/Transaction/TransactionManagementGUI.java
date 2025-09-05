package org.example.ver1.Transaction;

import org.example.ver1.Member.Member;
import org.example.ver1.Member.MemberDao;
import org.example.ver1.Member.MemberDaoImplementation;
import org.example.ver1.Member.MemberManagementGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TransactionManagementGUI extends JFrame {
    private final TransactionDao transactionDao;
    private final MemberDao memberDao;
    private JComboBox<String> memberComboBox;
    private JTextField amountField, descriptionField, dateField, idField;
    private JComboBox<String> typeComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, refreshButton, clearButton, membersButton, logoutButton;
    private JLabel balanceLabel, loanBalanceLabel;
    private JButton showBalanceButton;
    private JLabel totalBalanceLabel;

    public TransactionManagementGUI() {
        transactionDao = new TransactionDaoImplementation();
        memberDao = new MemberDaoImplementation();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadMembers();
        refreshTable();
    }

    private void initializeComponents() {
        setTitle("Cooperative Management System - Transactions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);

        // Form fields
        memberComboBox = new JComboBox<>();
        memberComboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));

        amountField = new JTextField(30);
        descriptionField = new JTextField(30);
        dateField = new JTextField(30);
        idField = new JTextField(30);
        idField.setEditable(false);

        typeComboBox = new JComboBox<>(new String[]{"DEPOSIT", "WITHDRAWAL", "LOAN", "PAYMENT"});
        typeComboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JTextField[] textFieldInputs = { amountField, descriptionField, dateField, idField };
        for (JTextField textFieldInput : textFieldInputs) {
            textFieldInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        // Balance display components
        balanceLabel = new JLabel("Current Balance: Not Selected");
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(34, 139, 34)); // Forest Green

        loanBalanceLabel = new JLabel("Outstanding Loan: Not Selected");
        loanBalanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        loanBalanceLabel.setForeground(new Color(220, 20, 60)); // Crimson

        showBalanceButton = new JButton("Show Balance");
        showBalanceButton.setPreferredSize(new Dimension(150, 35));
        showBalanceButton.setBackground(new Color(70, 130, 180));
        showBalanceButton.setForeground(Color.BLACK);
        showBalanceButton.setFocusPainted(false);
        showBalanceButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Buttons
        addButton = new JButton("Add Transaction");
        updateButton = new JButton("Update Transaction");
        deleteButton = new JButton("Delete Transaction");
        refreshButton = new JButton("Refresh");
        clearButton = new JButton("Clear Fields");
        membersButton = new JButton("Members");
        logoutButton = new JButton("Logout");

        // Style buttons
        Dimension buttonSize = new Dimension(200, 45);
        JButton[] buttons = { addButton, updateButton, deleteButton, refreshButton, clearButton, membersButton, logoutButton };
        Color[] colors = {
                new Color(34, 139, 34),    // Forest Green
                new Color(255, 140, 0),    // Dark Orange
                new Color(220, 20, 60),    // Crimson
                new Color(70, 130, 180),   // Steel Blue
                new Color(169, 169, 169),  // Dark Gray
                new Color(128, 0, 128),    // Purple
                new Color(255, 69, 0)      // Red Orange
        };

        for (byte i = 0; i < buttons.length; i++) {
            buttons[i].setPreferredSize(buttonSize);
            buttons[i].setMinimumSize(buttonSize);
            buttons[i].setMaximumSize(buttonSize);
            buttons[i].setBackground(colors[i]);
            buttons[i].setForeground(Color.BLACK);
            buttons[i].setFocusPainted(false);
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 14));
        }

        totalBalanceLabel = new JLabel("Total Balance: Not Selected");
        totalBalanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalBalanceLabel.setForeground(new Color(25, 25, 112)); // Navy Blue

        // Table setup
        String[] columnNames = {"ID", "Member", "Amount", "Type", "Description", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupTableStyling();

        // Add selection listener to table
        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = transactionTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFieldsFromTable(selectedRow);
                }
            }
        });
    }

    private void setupTableStyling() {
        // Set larger font for table content
        Font tableFont = new Font("SansSerif", Font.PLAIN, 14);
        transactionTable.setFont(tableFont);

        // Set row height to accommodate larger text
        transactionTable.setRowHeight(28);

        // Remove grid lines
        transactionTable.setShowGrid(false);
        transactionTable.setIntercellSpacing(new Dimension(0, 0));

        // Set table background and selection colors
        transactionTable.setBackground(Color.WHITE);
        transactionTable.setSelectionBackground(new Color(230, 240, 255)); // Light blue selection
        transactionTable.setSelectionForeground(Color.BLACK);

        // Style the header
        JTableHeader header = transactionTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBackground(new Color(173, 216, 230));
        header.setForeground(new Color(25, 25, 112));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 35));
        header.setBorder(BorderFactory.createEmptyBorder());
        header.setReorderingAllowed(false);

        // Create custom cell renderer for clean look
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Remove focus border
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

                // Alternate row colors for better readability
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248)); // Very light gray
                    }
                }

                return c;
            }
        };

        // Apply the renderer to all columns
        for (int i = 0; i < transactionTable.getColumnCount(); i++) {
            transactionTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Set column widths for better proportions
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Member
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Amount
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Type
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Description
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Date
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Create navigation panel at the top
        JPanel navigationPanel = createNavigationPanel();

        // Create main horizontal panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // LEFT PANEL - Buttons
        JPanel leftPanel = createButtonPanel();

        // CENTER PANEL - Table
        JPanel centerPanel = createTablePanel();

        // RIGHT PANEL - Form Fields
        JPanel rightPanel = createFormPanel();

        // Add panels to main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Add to frame
        add(navigationPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(new Color(70, 130, 180));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title on the left
        JLabel titleLabel = new JLabel("Transaction Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        // Navigation buttons on the right
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtonPanel.setBackground(new Color(70, 130, 180));

        // Style navigation buttons
        Dimension navButtonSize = new Dimension(120, 35);
        membersButton.setPreferredSize(new Dimension(200, 35));
        logoutButton.setPreferredSize(navButtonSize);

        navButtonPanel.add(membersButton);
        navButtonPanel.add(logoutButton);

        navPanel.add(titleLabel, BorderLayout.WEST);
        navPanel.add(navButtonPanel, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        buttonPanel.setPreferredSize(new Dimension(230, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        // Add some spacing at the top
        buttonPanel.add(Box.createVerticalStrut(30));

        // Add buttons with spacing
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        buttonPanel.add(updateButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        buttonPanel.add(deleteButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createVerticalStrut(20));

        buttonPanel.add(clearButton);

        // Add glue to push buttons to top
        buttonPanel.add(Box.createVerticalGlue());

        return buttonPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JScrollPane tableScrollPane = new JScrollPane(transactionTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 0));

        // Remove scroll pane border for cleaner look
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Transaction List"));

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Transaction Details"));
        formPanel.setPreferredSize(new Dimension(450, 0));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // ID Field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(idLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(idField, gbc);

        // Member ComboBox
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel memberLabel = new JLabel("Member:");
        memberLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(memberLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(memberComboBox, gbc);

        // Show Balance Button
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 5, 15);
        formPanel.add(showBalanceButton, gbc);

        // Balance Display Panel
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 8, 15);
        JPanel balancePanel = createBalancePanel();
        formPanel.add(balancePanel, gbc);

        // Amount Field
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 15, 8, 15);
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(amountLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(amountField, gbc);

        // Type ComboBox
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(typeLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(typeComboBox, gbc);

        // Description Field
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(descriptionLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(descriptionField, gbc);

        // Date Field
        gbc.gridx = 0; gbc.gridy = 12;
        gbc.fill = GridBagConstraints.NONE;
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(dateLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 13;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateField, gbc);

        // Add vertical glue to push form to top
        gbc.gridx = 0; gbc.gridy = 14;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        return formPanel;
    }

    private JPanel createBalancePanel() {
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.Y_AXIS));
        balancePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Member Balance"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)
        ));
        balancePanel.setBackground(new Color(248, 248, 255));

        balancePanel.add(balanceLabel);
        balancePanel.add(Box.createVerticalStrut(5));
        balancePanel.add(loanBalanceLabel);
        balancePanel.add(Box.createVerticalStrut(5));
        balancePanel.add(totalBalanceLabel);

        return balancePanel;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTransaction();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTransaction();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTransaction();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        showBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMemberBalance();
            }
        });

        // Navigation event handlers
        membersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateToMembers();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        // Auto-update balance when member selection changes
        memberComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (memberComboBox.getSelectedItem() != null) {
                    showMemberBalance();
                }
            }
        });
    }

    private void showMemberBalance() {
        String selectedMember = (String) memberComboBox.getSelectedItem();
        if (selectedMember == null) {
            balanceLabel.setText("Current Balance: No member selected");
            loanBalanceLabel.setText("Outstanding Loan: No member selected");
            totalBalanceLabel.setText("Total Balance: No member selected");
            return;
        }

        try {
            int memberId = extractMemberIdFromSelection(selectedMember);

            // Get current balance
            double currentBalance = transactionDao.calculateMemberBalance(memberId);

            // Get outstanding loan balance
            double outstandingLoan = transactionDao.getOutstandingLoanBalance(memberId);

            // Get total balance
            double totalBalance = transactionDao.calculateMemberTotalBalance(memberId);

            // Format and display the balances
            String balanceText = String.format("Current Balance: $%.2f", currentBalance);
            String loanText = String.format("Outstanding Loan: $%.2f", outstandingLoan);
            String totalText = String.format("Total Balance: $%.2f", totalBalance);

            balanceLabel.setText(balanceText);
            loanBalanceLabel.setText(loanText);
            totalBalanceLabel.setText(totalText);

            // Change color based on balance status
            if (currentBalance >= 0) {
                balanceLabel.setForeground(new Color(34, 139, 34)); // Green for positive
            } else {
                balanceLabel.setForeground(new Color(220, 20, 60)); // Red for negative
            }

            // Change loan color based on amount
            if (outstandingLoan > 0) {
                loanBalanceLabel.setForeground(new Color(220, 20, 60)); // Red for outstanding loans
            } else {
                loanBalanceLabel.setForeground(new Color(34, 139, 34)); // Green for no loans
            }

            // Change total balance color based on amount
            if (totalBalance >= 0) {
                totalBalanceLabel.setForeground(new Color(25, 25, 112)); // Navy blue for positive
            } else {
                totalBalanceLabel.setForeground(new Color(220, 20, 60)); // Red for negative
            }

        } catch (Exception e) {
            balanceLabel.setText("Current Balance: Error loading");
            loanBalanceLabel.setText("Outstanding Loan: Error loading");
            totalBalanceLabel.setText("Total Balance: Error loading");
            JOptionPane.showMessageDialog(this, "Error loading member balance: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void navigateToMembers() {
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            new MemberManagementGUI().setVisible(true);
        });
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> {
                this.dispose();
                // Assuming your main login class is in org.example.ver1.auth package
                try {
                    Class<?> mainGUIClass = Class.forName("org.example.ver1.auth.MainGUI");
                    mainGUIClass.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(0); // Fallback exit if reflection fails
                }
            });
        }
    }

    private void loadMembers() {
        try {
            List<Member> members = memberDao.fetchMembers();
            memberComboBox.removeAllItems();

            for (Member member : members) {
                String displayText = member.getFirstName() + " " + member.getLastName() + " (ID: " + member.getId() + ")";
                memberComboBox.addItem(displayText);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTransaction() {
        if (!validateFields(false)) return;

        try {
            // Extract member ID from combo box selection
            String selectedMember = (String) memberComboBox.getSelectedItem();
            if (selectedMember == null) {
                JOptionPane.showMessageDialog(this, "Please select a member!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extract ID from the display text (assuming format: "Name (ID: X)")
            int memberId = extractMemberIdFromSelection(selectedMember);

            Transaction transaction = Transaction.builder()
                    .memberId(memberId)
                    .amount(Double.parseDouble(amountField.getText().trim()))
                    .type(typeComboBox.getSelectedItem().toString())
                    .description(descriptionField.getText().trim())
                    .date(dateField.getText().trim())
                    .build();

            boolean result = transactionDao.insertTransaction(transaction);
            if (result) {
                JOptionPane.showMessageDialog(this, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
                showMemberBalance(); // Update balance display after successful transaction
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add transaction!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTransaction() {
        if (!validateFields(true)) return;

        try {
            // Extract member ID from combo box selection
            String selectedMember = (String) memberComboBox.getSelectedItem();
            if (selectedMember == null) {
                JOptionPane.showMessageDialog(this, "Please select a member!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int memberId = extractMemberIdFromSelection(selectedMember);

            Transaction transaction = Transaction.builder()
                    .id(Integer.parseInt(idField.getText().trim()))
                    .memberId(memberId)
                    .amount(Double.parseDouble(amountField.getText().trim()))
                    .type(typeComboBox.getSelectedItem().toString())
                    .description(descriptionField.getText().trim())
                    .date(dateField.getText().trim())
                    .build();

            boolean result = transactionDao.updateTransaction(transaction);
            if (result) {
                JOptionPane.showMessageDialog(this, "Transaction updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
                showMemberBalance(); // Update balance display after successful transaction
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update transaction!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this transaction?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Transaction transaction = Transaction.builder()
                        .id(Integer.parseInt(idField.getText().trim()))
                        .build();

                boolean result = transactionDao.deleteTransaction(transaction);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Transaction deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                    showMemberBalance(); // Update balance display after successful deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete transaction!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        try {
            List<Transaction> transactions = transactionDao.fetchTransactions();
            tableModel.setRowCount(0); // Clear existing data

            for (Transaction transaction : transactions) {
                // Get member name for display
                String memberName = getMemberNameById(transaction.getMemberId());

                Object[] row = {
                        transaction.getId(),
                        memberName,
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getDescription(),
                        transaction.getDate()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading transactions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getMemberNameById(int memberId) {
        try {
            List<Member> members = memberDao.fetchMembers();
            for (Member member : members) {
                if (member.getId() == memberId) {
                    return member.getFirstName() + " " + member.getLastName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown Member";
    }

    private int extractMemberIdFromSelection(String selection) {
        // Extract ID from format: "Name (ID: X)"
        int startIndex = selection.lastIndexOf("ID: ") + 4;
        int endIndex = selection.lastIndexOf(")");
        return Integer.parseInt(selection.substring(startIndex, endIndex));
    }

    private void populateFieldsFromTable(int selectedRow) {
        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());

        // Set member combo box based on the member name in the table
        String memberName = tableModel.getValueAt(selectedRow, 1).toString();
        for (int i = 0; i < memberComboBox.getItemCount(); i++) {
            String item = memberComboBox.getItemAt(i);
            if (item.startsWith(memberName)) {
                memberComboBox.setSelectedIndex(i);
                break;
            }
        }

        amountField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        typeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
        descriptionField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        dateField.setText(tableModel.getValueAt(selectedRow, 5).toString());

        // Update balance display when a row is selected
        showMemberBalance();
    }

    private void clearFields() {
        idField.setText("");
        memberComboBox.setSelectedIndex(-1);
        amountField.setText("");
        typeComboBox.setSelectedIndex(0);
        descriptionField.setText("");
        dateField.setText("");
        transactionTable.clearSelection();

        // Reset balance display
        balanceLabel.setText("Current Balance: Not Selected");
        loanBalanceLabel.setText("Outstanding Loan: Not Selected");
        totalBalanceLabel.setText("Total Balance: Not Selected");
        balanceLabel.setForeground(new Color(34, 139, 34));
        loanBalanceLabel.setForeground(new Color(220, 20, 60));
        totalBalanceLabel.setForeground(new Color(25, 25, 112));
    }

    private boolean validateFields(boolean requireId) {
        if (requireId && idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID is required for update/delete operations!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (memberComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a member!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            memberComboBox.requestFocus();
            return false;
        }

        if (amountField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                amountField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            amountField.requestFocus();
            return false;
        }

        if (dateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date is required (format: YYYY-MM-DD)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }

        // Basic date format validation
        String date = dateField.getText().trim();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                new TransactionManagementGUI().setVisible(true);
            }
        });
    }
}