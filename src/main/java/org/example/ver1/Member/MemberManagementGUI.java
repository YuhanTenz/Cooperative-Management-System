package org.example.ver1.Member;

import org.example.ver1.Transaction.TransactionManagementGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MemberManagementGUI extends JFrame {
    private final MemberDao memberDao;
    private JTextField firstNameField, middleNameField, lastNameField, addressField, birthdateField, idField;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, deleteButton, refreshButton, clearButton, transactionButton, logoutButton;

    public MemberManagementGUI() {
        memberDao = new MemberDaoImplementation();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        refreshTable();
    }

    private void initializeComponents() {
        setTitle("Cooperative Management System - Members");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Text fields
        firstNameField = new JTextField(30);
        middleNameField = new JTextField(30);
        lastNameField = new JTextField(30);
        addressField = new JTextField(30);
        birthdateField = new JTextField(30);
        idField = new JTextField(30);
        idField.setEditable(false);

        JTextField[] textFieldInputs = { firstNameField, middleNameField, lastNameField, addressField, birthdateField, idField };
        for (JTextField textFieldInput : textFieldInputs) {
            textFieldInput.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        // Buttons
        addButton = new JButton("Add Member");
        updateButton = new JButton("Update Member");
        deleteButton = new JButton("Delete Member");
        refreshButton = new JButton("Refresh");
        clearButton = new JButton("Clear Fields");
        transactionButton = new JButton("Transactions");
        logoutButton = new JButton("Logout");

        // Style buttons
        Dimension buttonSize = new Dimension(150, 45);
        JButton[] buttons = { addButton, updateButton, deleteButton, refreshButton, transactionButton, clearButton, logoutButton };
        Color[] colors = {
                new Color(34, 139, 34),    // Forest Green
                new Color(255, 140, 0),    // Dark Orange
                new Color(220, 20, 60),    // Crimson
                new Color(70, 130, 180),   // Steel Blue
                new Color(169, 169, 169),  // Dark Gray
                new Color(138, 43, 226),   // Blue Violet
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

        // Table setup
        String[] columnNames = {"ID", "First Name", "Middle Name", "Last Name", "Address", "Birthdate"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupTableStyling();

        // Add selection listener to table
        memberTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = memberTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFieldsFromTable(selectedRow);
                }
            }
        });
    }

    private void setupTableStyling() {
        // Set larger font for table content
        Font tableFont = new Font("SansSerif", Font.PLAIN, 14);
        memberTable.setFont(tableFont);

        // Set row height to accommodate larger text
        memberTable.setRowHeight(28);

        // Remove grid lines
        memberTable.setShowGrid(false);
        memberTable.setIntercellSpacing(new Dimension(0, 0));

        // Set table background and selection colors
        memberTable.setBackground(Color.WHITE);
        memberTable.setSelectionBackground(new Color(230, 240, 255)); // Light blue selection
        memberTable.setSelectionForeground(Color.BLACK);

        // Style the header
        JTableHeader header = memberTable.getTableHeader();
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
        for (int i = 0; i < memberTable.getColumnCount(); i++) {
            memberTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Set column widths for better proportions
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(120); // First Name
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Middle Name
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Last Name
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(200); // Address
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Birthdate
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
        JLabel titleLabel = new JLabel("Member Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        // Navigation buttons on the right
        JPanel navButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navButtonPanel.setBackground(new Color(70, 130, 180));

        // Style navigation buttons
        Dimension navButtonSize = new Dimension(120, 35);
        transactionButton.setPreferredSize(new Dimension(200, 35));
        logoutButton.setPreferredSize(navButtonSize);

        navButtonPanel.add(transactionButton);
        navButtonPanel.add(logoutButton);

        navPanel.add(titleLabel, BorderLayout.WEST);
        navPanel.add(navButtonPanel, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        buttonPanel.setPreferredSize(new Dimension(180, 0));
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

        JScrollPane tableScrollPane = new JScrollPane(memberTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 0));

        // Remove scroll pane border for cleaner look
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Member List"));

        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Member Details"));
        formPanel.setPreferredSize(new Dimension(400, 0));
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

        // First Name Field
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(firstNameLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(firstNameField, gbc);

        // Middle Name Field
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        JLabel middleNameLabel = new JLabel("Middle Name:");
        middleNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(middleNameLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(middleNameField, gbc);

        // Last Name Field
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(lastNameLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lastNameField, gbc);

        // Address Field
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(addressField, gbc);

        // Birthdate Field
        gbc.gridx = 0; gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        JLabel birthdateLabel = new JLabel("Birthdate (YYYY-MM-DD):");
        birthdateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        formPanel.add(birthdateLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(birthdateField, gbc);

        // Add vertical glue to push form to top
        gbc.gridx = 0; gbc.gridy = 12;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        return formPanel;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMember();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMember();
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

        // Navigation event handlers
        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateToTransactions();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void navigateToTransactions() {
        SwingUtilities.invokeLater(() -> {
            this.dispose();
            new TransactionManagementGUI().setVisible(true);
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

    private void addMember() {
        if (!validateFields(false)) return;

        try {
            Member member = Member.builder()
                    .firstName(firstNameField.getText().trim())
                    .middleName(middleNameField.getText().trim())
                    .lastName(lastNameField.getText().trim())
                    .address(addressField.getText().trim())
                    .birthdate(birthdateField.getText().trim())
                    .build();

            boolean result = memberDao.insertMember(member);
            if (result) {
                JOptionPane.showMessageDialog(this, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add member!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMember() {
        if (!validateFields(true)) return;

        try {
            Member member = Member.builder()
                    .id(Integer.parseInt(idField.getText().trim()))
                    .firstName(firstNameField.getText().trim())
                    .middleName(middleNameField.getText().trim())
                    .lastName(lastNameField.getText().trim())
                    .address(addressField.getText().trim())
                    .birthdate(birthdateField.getText().trim())
                    .build();

            boolean result = memberDao.updateMember(member);
            if (result) {
                JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update member!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMember() {
        if (idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Member member = Member.builder()
                        .id(Integer.parseInt(idField.getText().trim()))
                        .build();

                boolean result = memberDao.deleteMember(member);
                if (result) {
                    JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete member!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        try {
            List<Member> members = memberDao.fetchMembers();
            tableModel.setRowCount(0); // Clear existing data

            for (Member member : members) {
                Object[] row = {
                        member.getId(),
                        member.getFirstName(),
                        member.getMiddleName(),
                        member.getLastName(),
                        member.getAddress(),
                        member.getBirthdate()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFieldsFromTable(int selectedRow) {
        idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        firstNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        middleNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        lastNameField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        addressField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        birthdateField.setText(tableModel.getValueAt(selectedRow, 5).toString());
    }

    private void clearFields() {
        idField.setText("");
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        birthdateField.setText("");
        memberTable.clearSelection();
    }

    private boolean validateFields(boolean requireId) {
        if (requireId && idField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID is required for update/delete operations!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }

        if (birthdateField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Birthdate is required (format: YYYY-MM-DD)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            birthdateField.requestFocus();
            return false;
        }

        // Basic date format validation
        String birthdate = birthdateField.getText().trim();
        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Validation Error", JOptionPane.ERROR_MESSAGE);
            birthdateField.requestFocus();
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

                new MemberManagementGUI().setVisible(true);
            }
        });
    }
}