

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package view;

import view.theme.UITheme;
import view.theme.UIComponents;
import service.EmployeeService;
import model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import model.EmployeeStatus;
import model.PayrollResult;
import service.PayrollService;
import JasperReports.ReportGenerator;
/**
 *
 * @author Amir
 */
public class MainFrame extends JFrame {
 
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private EmployeeService employeeDetails;
 
    private JButton viewEmployeeBtn, newEmployeeBtn, updateEmployeeBtn, deleteEmployeeBtn;
    private JButton refreshBtn, logoutBtn, manageLeavesBtn;
 
    private EmployeeFormPanel detailPanel;
 
    private Employee currentUser;
    private Employee selectedEmployee = null;
    private EmployeeDetailFrame detailFrame = null;
    private AddEmployeeFrame addEmployeeFrame = null;
    private LeaveManagementFrame leaveManagementFrame = null;
 
    public MainFrame(Employee user) {
        this.currentUser = user;
        this.employeeDetails = EmployeeService.getInstance();
 
        initializeGUI();
 
        loadEmployeeData();
 
        setTitle("MotorPH - Hello, " + currentUser.getFirstName());
 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    System.out.println("Employee data successfully saved on shutdown.");
                } catch (Exception ex) {
                    System.err.println("Error saving employee data: " + ex.getMessage());
                }
            }
        });
    }
 
    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_LIGHT_GRAY);
 
        JPanel bottomBar = createBottomActionBar();
        add(bottomBar, BorderLayout.SOUTH);
 
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UITheme.BG_WHITE);
        headerPanel.setPreferredSize(new Dimension(1400, 65));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_GRAY));
 
        JLabel lblLogo = new JLabel("   WELCOME TO MOTORPH MANAGEMENT SYSTEM");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(UITheme.TEXT_PRIMARY);
 
        JLabel lblUser = new JLabel("Logged in as: " + currentUser.getFirstName() + " " + currentUser.getLastName() + "  ");
        lblUser.setFont(UITheme.FONT_SUBHEADING);
 
        headerPanel.add(lblLogo, BorderLayout.WEST);
        headerPanel.add(lblUser, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
 
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.PRIMARY);
        sidebar.setPreferredSize(new Dimension(240, 800));
 
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
 
        sidebar.add(createNavButton("Employee Records", e -> {
            employeeTable.clearSelection();
            loadEmployeeData();
            detailPanel.clearForm();
            JOptionPane.showMessageDialog(this, "Employee Records Refreshed.");
        }));
 
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
 
        // admin/finance
        if (currentUser.canEditFinancials()) {
            sidebar.add(createNavButton("Payroll Center", e -> {
                String selectedId = JOptionPane.showInputDialog(this, "Enter Employee ID:", "Generate Payslip", JOptionPane.QUESTION_MESSAGE);
                if (selectedId == null || selectedId.trim().isEmpty()) return;
 
                if (employeeDetails.findEmployeeById(selectedId) == null) {
                    JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                String[] years = {"2024", "2025", "2026"};
 
                JComboBox<String> monthBox = new JComboBox<>(months);
                JComboBox<String> yearBox = new JComboBox<>(years);
 
                selectionPanel.add(new JLabel("Month:"));
                selectionPanel.add(monthBox);
                selectionPanel.add(new JLabel("Year:"));
                selectionPanel.add(yearBox);
 
                int result = JOptionPane.showConfirmDialog(this, selectionPanel, "Select Payroll Period", JOptionPane.OK_CANCEL_OPTION);
 
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int month = monthBox.getSelectedIndex() + 1;
                        int year = Integer.parseInt((String) yearBox.getSelectedItem());
 
                        JasperReports.PayslipReportGenerator payslipGen = new JasperReports.PayslipReportGenerator();
                        java.sql.Connection conn = config.DatabaseConnection.getConnection();
 
                        payslipGen.generateIndividualPayslip(selectedId, month, year);
 
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error generating payroll: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }));
 
            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
 
            //  payroll summary
            String[] departments = {"Leadership", "HR", "IT", "Accounting", "Marketing"};
            JButton viewReportBtn = createNavButton("View Payroll Summary", e -> {
                try {
                    String selectedDept = (String) JOptionPane.showInputDialog(
                            this,
                            "Select a department to generate the payroll summary report:",
                            "Select Department",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            departments,
                            departments[0]
                    );
 
                    if (selectedDept == null) return;
 
                    java.sql.Connection conn = config.DatabaseConnection.getConnection();
                    JasperReports.ReportGenerator generator = new JasperReports.ReportGenerator();
                    generator.generateReport(conn, selectedDept);
 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            sidebar.add(viewReportBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }
 
        // jasper reports hr/fin/admin
        if (currentUser.canEditFinancials() || currentUser.canEditBasicInfo()) {
            sidebar.add(createNavButton("View Time Card", e -> {
                String empId = JOptionPane.showInputDialog(this, "Enter Employee ID:", "Time Card Request", JOptionPane.QUESTION_MESSAGE);
                if (empId == null || empId.trim().isEmpty()) return;
 
                if (employeeDetails.findEmployeeById(empId) == null) {
                    JOptionPane.showMessageDialog(this, "Employee ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
 
                JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                String[] years = {"2024", "2025", "2026"};
                JComboBox<String> monthBox = new JComboBox<>(months);
                JComboBox<String> yearBox = new JComboBox<>(years);
 
                selectionPanel.add(new JLabel("Month:")); selectionPanel.add(monthBox);
                selectionPanel.add(new JLabel("Year:")); selectionPanel.add(yearBox);
 
                int result = JOptionPane.showConfirmDialog(this, selectionPanel, "Select Period", JOptionPane.OK_CANCEL_OPTION);
 
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int month = monthBox.getSelectedIndex() + 1;
                        int year = Integer.parseInt((String) yearBox.getSelectedItem());
 
                        JasperReports.EmployeeTimeCardReportGenerator timeCardGen = new JasperReports.EmployeeTimeCardReportGenerator();
                        timeCardGen.generateTimeCard(empId, month, year);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }));
            sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        }
 
        // Standard Sidebar Action buttons
        manageLeavesBtn = createNavButton("Manage Leaves", new ManageLeavesListener());
        sidebar.add(manageLeavesBtn);
 
        sidebar.add(Box.createVerticalGlue());
 
        logoutBtn = createNavButton("Logout System", new LogoutEmployeeListener());
        sidebar.add(logoutBtn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
 
        add(sidebar, BorderLayout.WEST);
 
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(850);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        splitPane.setLeftComponent(createTablePanel());
 
        detailPanel = new EmployeeFormPanel();
        detailPanel.setFieldsEditable(false, false);
        splitPane.setRightComponent(detailPanel);
 
        add(splitPane, BorderLayout.CENTER);
 
        applySecuritySettings();
 
        setSize(1400, 850);
        setLocationRelativeTo(null);
    }
 
    private JPanel createBottomActionBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(UITheme.BG_WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
 
        newEmployeeBtn = UIComponents.primaryButton("NEW EMPLOYEE");
        newEmployeeBtn.addActionListener(new NewEmployeeListener());
 
        updateEmployeeBtn = UIComponents.styledButton("SAVE UPDATES", UITheme.WARNING);
        updateEmployeeBtn.setForeground(UITheme.TEXT_PRIMARY);
        updateEmployeeBtn.addActionListener(new UpdateEmployeeListener());
        updateEmployeeBtn.setEnabled(false);
 
        deleteEmployeeBtn = UIComponents.dangerButton("DELETE");
        deleteEmployeeBtn.addActionListener(new DeleteEmployeeListener());
        deleteEmployeeBtn.setEnabled(false);
 
        viewEmployeeBtn = UIComponents.secondaryButton("VIEW FULL DETAILS");
        viewEmployeeBtn.addActionListener(new ViewEmployeeListener());
 
        refreshBtn = UIComponents.secondaryButton("REFRESH");
        refreshBtn.addActionListener(e -> loadEmployeeData());
 
        panel.add(newEmployeeBtn);
        panel.add(updateEmployeeBtn);
        panel.add(deleteEmployeeBtn);
        panel.add(new JSeparator(JSeparator.VERTICAL));
        panel.add(viewEmployeeBtn);
        panel.add(refreshBtn);
 
        return panel;
    }
 
    private JButton createNavButton(String text, ActionListener action) {
        JButton btn = new JButton("   " + text);
        btn.setMaximumSize(new Dimension(240, 50));
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setForeground(new Color(236, 240, 241));
        btn.setBackground(UITheme.PRIMARY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
 
        if (action != null) btn.addActionListener(action);
 
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(UITheme.PRIMARY_LIGHT.darker()); }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(UITheme.PRIMARY); }
        });
        return btn;
    }
 
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Employee Records"));
 
        String[] columnNames = {
            "Employee #", "Last Name", "First Name", "SSS Number",
            "PhilHealth Number", "TIN", "Pag-IBIG Number"
        };
 
        tableModel = UIComponents.readOnlyModel(columnNames);
 
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(UITheme.TABLE_ROW_HEIGHT + 2);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.setFont(UITheme.FONT_TABLE);
        employeeTable.getTableHeader().setFont(UITheme.FONT_TABLE_HEAD);
        employeeTable.getTableHeader().setBackground(UITheme.PRIMARY);
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        employeeTable.setSelectionForeground(Color.WHITE);
 
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelectedRow();
            }
        });
 
        tablePanel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        return tablePanel;
    }
 
    private void populateFormFromSelectedRow() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            selectedEmployee = null;
            detailPanel.clearForm();
            detailPanel.setFieldsEditable(currentUser.canEditBasicInfo(), currentUser.canEditFinancials());
            updateEmployeeBtn.setEnabled(false);
            deleteEmployeeBtn.setEnabled(false);
            return;
        }
 
        String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
        selectedEmployee = employeeDetails.findEmployeeById(employeeId);
 
        if (selectedEmployee != null) {
            detailPanel.setEmployeeData(selectedEmployee);
            detailPanel.setFieldsEditable(currentUser.canEditBasicInfo(), currentUser.canEditFinancials());
 
            updateEmployeeBtn.setEnabled(currentUser.canEditBasicInfo() || currentUser.canEditFinancials());
            deleteEmployeeBtn.setEnabled(currentUser.canDeleteEmployee());
            viewEmployeeBtn.setEnabled(true);
        }
    }
 
    private void loadEmployeeData() {
        if (tableModel == null) return;
        tableModel.setRowCount(0);
        boolean canViewAll = currentUser.canViewAllRecords();
 
        List<Employee> employees = employeeDetails.getAllEmployees();
        for (Employee emp : employees) {
            if (!canViewAll && !emp.getEmployeeId().equals(currentUser.getEmployeeId())) {
                continue;
            }
 
            Object[] rowData = {
                emp.getEmployeeId(), emp.getLastName(), emp.getFirstName(),
                emp.getSssNumber(), emp.getPhilHealth(), emp.getTinNumber(), emp.getPagIbig()
            };
            tableModel.addRow(rowData);
        }
    }
 
    private void applySecuritySettings() {
        boolean canView = currentUser.canViewDatabase();
        boolean canAdd = currentUser.canAddEmployee();
        boolean canUpdate = currentUser.canEditBasicInfo() || currentUser.canEditFinancials();
        boolean canDelete = currentUser.canDeleteEmployee();
        boolean canApprove = currentUser.canApproveLeave();
 
        newEmployeeBtn.setVisible(canAdd);
        deleteEmployeeBtn.setVisible(canDelete);
        updateEmployeeBtn.setVisible(canUpdate);
        manageLeavesBtn.setVisible(canApprove);
 
        if (!canView) {
            JOptionPane.showMessageDialog(this, "Access Denied: You do not have permission to view the employee database.");
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
 
    public void refreshTable() {
        loadEmployeeData();
    }
 
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "MotoPH Employee Management System\n" +
            "Version 2.0\n" +
            "Employee record management with update and delete functionality",
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
 
    // Action Listeners
    private class UpdateEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee == null) return;
 
            try {
                String[] data = detailPanel.validateAndGetFormData();
                Employee updatedEmployee = EmployeeStatus.createFromDb(data);
 
                if (updatedEmployee != null && employeeDetails.updateEmployee(updatedEmployee)) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Employee updated successfully!");
                    loadEmployeeData();
                    selectedEmployee = updatedEmployee;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainFrame.this, "Update Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    private class DeleteEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(MainFrame.this, "Please select an employee first.");
                return;
            }
 
            int result = JOptionPane.showConfirmDialog(
                MainFrame.this,
                "Are you sure you want to delete " + selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
 
            if (result == JOptionPane.YES_OPTION) {
                try {
                    if (employeeDetails.deleteEmployee(selectedEmployee.getEmployeeId())) {
                        JOptionPane.showMessageDialog(MainFrame.this, "Employee deleted successfully.");
                        loadEmployeeData();
                        detailPanel.clearForm();
                        selectedEmployee = null;
                    } else {
                        JOptionPane.showMessageDialog(MainFrame.this, "Delete failed: Employee ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalStateException ex) {
                    JOptionPane.showMessageDialog(
                        MainFrame.this,
                        ex.getMessage(),
                        "Security Access Denied",
                        JOptionPane.ERROR_MESSAGE
                    );
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainFrame.this, "An unexpected error occurred: " + ex.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
 
    private class ViewEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEmployee != null) {
                if (detailFrame == null || !detailFrame.isDisplayable()) {
                    detailFrame = new EmployeeDetailFrame(selectedEmployee, currentUser, true);
                    detailFrame.setVisible(true);
                } else {
                    detailFrame.toFront();
                    detailFrame.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(MainFrame.this,
                    "Please select an employee from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
 
    private class NewEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (addEmployeeFrame == null || !addEmployeeFrame.isDisplayable()) {
                addEmployeeFrame = new AddEmployeeFrame(MainFrame.this, employeeDetails);
                addEmployeeFrame.setVisible(true);
            } else {
                addEmployeeFrame.toFront();
                addEmployeeFrame.requestFocus();
            }
        }
    }
 
    private class ManageLeavesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (leaveManagementFrame == null || !leaveManagementFrame.isDisplayable()) {
                leaveManagementFrame = new LeaveManagementFrame();
                leaveManagementFrame.setVisible(true);
            } else {
                leaveManagementFrame.toFront();
                leaveManagementFrame.requestFocus();
            }
        }
    }
 
    private class LogoutEmployeeListener implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(MainFrame.this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                MainFrame.this.dispose();
                if (detailFrame != null) detailFrame.dispose();
                if (addEmployeeFrame != null) addEmployeeFrame.dispose();
                if (leaveManagementFrame != null) leaveManagementFrame.dispose();
 
                new LoginFrame().setVisible(true);
            }
        }
    }
}
