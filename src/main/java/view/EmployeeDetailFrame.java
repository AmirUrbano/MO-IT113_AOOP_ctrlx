/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;



import view.theme.UITheme;
import view.theme.UIComponents;
import model.Employee;
import model.LeaveRequest;
import model.AttendanceRecord;
import service.EmployeeService;
import service.PayrollService;
import service.LeaveService;
import service.AttendanceService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/**
 *
 * @author Amir
 */

  public class EmployeeDetailFrame extends JFrame {
    private final Employee employee;
    private final Employee loggedInUser;
    private final EmployeeService employeeService;
    private final PayrollService payrollProcessor;
    private final boolean isReadOnlyMode;
 
    private JComboBox<String> monthComboBox;
    private JComboBox<String> yearComboBox;
    private JTextPane payrollResultArea;
    private JButton computeBtn;
    private JButton viewPdfPayslipBtn;
    private JButton timeInBtn;
    private JButton timeOutBtn;
    private JLabel summaryLabel;
    private DefaultTableModel leaveHistoryModel;
    private JPanel attendancePanel;
    private JButton applyLeaveBtn;
    private JButton logoutBtn;
    private JPanel payrollSectionPanel;
    private JScrollPane leaveHistoryScroll;
 
    public EmployeeDetailFrame(Employee employee, Employee loggedInUser, boolean readOnly) {
        this.employee = employee;
        this.loggedInUser = loggedInUser;
        this.isReadOnlyMode = readOnly;
        this.employeeService = EmployeeService.getInstance();
        this.payrollProcessor = PayrollService.getInstance();
 
        initializeGUI();
        applySecurityVisibility();
        refreshAllData();
    }
 
    private void initializeGUI() {
        setTitle("MotorPH Employee Portal - " + employee.getFirstName() + " " + employee.getLastName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 900);
        setLayout(new BorderLayout(15, 15));
        setLocationRelativeTo(null);
 
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(createSummaryCard(), BorderLayout.NORTH);
        northPanel.add(createInfoCardPanel(), BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
 
        JPanel centerContainer = new JPanel(new BorderLayout(10, 10));
        centerContainer.add(createPayrollPanel(), BorderLayout.CENTER);
        centerContainer.add(createAttendancePanel(), BorderLayout.SOUTH);
        add(centerContainer, BorderLayout.CENTER);
 
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
 
    private JPanel createSummaryCard() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        summaryPanel.setBackground(UITheme.PRIMARY);
        summaryLabel = new JLabel("Syncing with MotorPH Database...");
        summaryLabel.setForeground(Color.WHITE);
        summaryLabel.setFont(UITheme.FONT_LABEL);
        summaryPanel.add(summaryLabel);
        return summaryPanel;
    }
 
    private JPanel createInfoCardPanel() {
        JPanel mainHeaderPanel = new JPanel(new BorderLayout(10, 10));
        mainHeaderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Employment Info
        JPanel employmentPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        employmentPanel.setBorder(BorderFactory.createTitledBorder("Employment Information"));
        employmentPanel.add(createCard("Employee ID", employee.getEmployeeId()));
        employmentPanel.add(createCard("Status", employee.getStatus()));
        employmentPanel.add(createCard("Position", employee.getPosition()));
        employmentPanel.add(createCard("Supervisor", employee.getSupervisor()));
        employmentPanel.add(createCard("SSS #", employee.getSssNumber()));
        employmentPanel.add(createCard("TIN #", employee.getTinNumber()));
        employmentPanel.add(createCard("PhilHealth #", employee.getPhilHealth()));
        employmentPanel.add(createCard("Pag-IBIG #", employee.getPagIbig()));
 
        // Financials
        JPanel financialPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        financialPanel.setBorder(BorderFactory.createTitledBorder("Salary & Rates"));
        financialPanel.add(createCard("Basic Salary", "PHP " + String.format("%.2f", employee.getBasicSalary())));
        financialPanel.add(createCard("Hourly Rate", "PHP " + String.format("%.2f", employee.getHourlyRate())));
        financialPanel.add(createCard("Rice Subsidy", "PHP " + String.format("%.2f", employee.getRiceSubsidy())));
        financialPanel.add(createCard("Phone Allowance", "PHP " + String.format("%.2f", employee.getPhoneAllowance())));
        financialPanel.add(createCard("Clothing", "PHP " + String.format("%.2f", employee.getClothingAllowance())));
        mainHeaderPanel.add(employmentPanel, BorderLayout.NORTH);
        mainHeaderPanel.add(financialPanel, BorderLayout.SOUTH);
        return mainHeaderPanel;
    }
 
    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        JLabel titleLbl = new JLabel(title.toUpperCase());
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        titleLbl.setForeground(UITheme.TEXT_MUTED);
        JLabel valueLbl = new JLabel(value != null ? value : "N/A");
        valueLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        card.add(titleLbl, BorderLayout.NORTH);
        card.add(valueLbl, BorderLayout.CENTER);
        return card;
    }
 
    private JPanel createAttendancePanel() {
        attendancePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        attendancePanel.setBorder(BorderFactory.createTitledBorder("Daily Attendance Control"));
        timeInBtn = UIComponents.styledButton("TIME IN", new Color(46, 204, 113));
        timeOutBtn = UIComponents.styledButton("TIME OUT", new Color(231, 76, 60));
 
        timeInBtn.addActionListener(e -> handleAttendance(true));
        timeOutBtn.addActionListener(e -> handleAttendance(false));
 
        attendancePanel.add(timeInBtn);
        attendancePanel.add(timeOutBtn);
        return attendancePanel;
    }
 
    private void handleAttendance(boolean isTimeIn) {
        String msg = AttendanceService.getInstance().recordAttendance(employee.getEmployeeId(), isTimeIn);
        JOptionPane.showMessageDialog(this, msg);
        refreshAllData();
    }
 
    private void refreshAllData() {
        leaveHistoryModel.setRowCount(0);
 
        List<LeaveRequest> history = LeaveService.getInstance().getLeaveHistoryForEmployee(employee.getEmployeeId());
        for (LeaveRequest lr : history) {
            leaveHistoryModel.addRow(new Object[]{lr.getRequestId(), lr.getLeaveType(), lr.getStartDate(), lr.getEndDate(), lr.getStatus()});
        }
 
        LocalDate today = LocalDate.now();
        List<AttendanceRecord> records = AttendanceService.getInstance().getRecordsInRange(employee.getEmployeeId(), today, today);
 
        if (records.isEmpty()) {
            timeInBtn.setEnabled(true);
            timeOutBtn.setEnabled(false);
        } else {
            timeInBtn.setEnabled(false);
            AttendanceRecord latest = records.get(records.size() - 1);
            boolean alreadyTimedOut = latest.getLogOutTime() != null &&
                             !latest.getLogOutTime().toString().equals("00:00");
            timeOutBtn.setEnabled(!alreadyTimedOut);
        }
 
        double allowances = employee.getRiceSubsidy() + employee.getPhoneAllowance() + employee.getClothingAllowance();
        summaryLabel.setText("Monthly Fixed Benefits Total: PHP " + String.format("%.2f", allowances));
    }
 
    private JPanel createPayrollPanel() {
        payrollSectionPanel = new JPanel(new BorderLayout(10, 10));
        payrollSectionPanel.setBorder(BorderFactory.createTitledBorder("Payroll Calculation"));
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
 
        monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        monthComboBox.setSelectedIndex(5);
 
        yearComboBox = new JComboBox<>(new String[]{"2024", "2025", "2026"});
        yearComboBox.setSelectedItem("2024");
 
        computeBtn = UIComponents.secondaryButton("Generate Payroll");
        computeBtn.addActionListener(new ComputePayrollListener());
 
        viewPdfPayslipBtn = UIComponents.infoButton("View PDF Payslip");
        viewPdfPayslipBtn.addActionListener(e -> generateJasperPayslip());
 
        controls.add(new JLabel("Select Month:"));
        controls.add(monthComboBox);
        controls.add(new JLabel("Select Year:"));
        controls.add(yearComboBox);
        controls.add(computeBtn);
        controls.add(viewPdfPayslipBtn);
 
        payrollResultArea = new JTextPane();
        payrollResultArea.setEditable(false);
        payrollResultArea.setFont(UITheme.FONT_MONO);
 
        StyledDocument doc = payrollResultArea.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
 
        payrollSectionPanel.add(controls, BorderLayout.NORTH);
        payrollSectionPanel.add(new JScrollPane(payrollResultArea), BorderLayout.CENTER);
        return payrollSectionPanel;
    }
 
    private void generateJasperPayslip() {
        int month = monthComboBox.getSelectedIndex() + 1;
        int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        String targetEmployeeId = employee.getEmployeeId();
 
        try {
            payrollProcessor.exportPayslipPDF(targetEmployeeId, month, year, loggedInUser);
            JOptionPane.showMessageDialog(this, "Jasper PDF Payslip loaded successfully!", "Report Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SecurityException secEx) {
            JOptionPane.showMessageDialog(this, secEx.getMessage(), "Security Exception", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "System Error loading print engine: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
 
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        String[] leaveColumns = {"ID", "Type", "Start", "End", "Status"};
        leaveHistoryModel = UIComponents.readOnlyModel(leaveColumns);
        JTable leaveTable = new JTable(leaveHistoryModel);
        setupTableVisuals(leaveTable);
        leaveHistoryScroll = new JScrollPane(leaveTable);
        leaveHistoryScroll.setPreferredSize(new Dimension(0, 150));
        leaveHistoryScroll.setBorder(BorderFactory.createTitledBorder("My Leave Requests"));
        panel.add(leaveHistoryScroll, BorderLayout.CENTER);
 
        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        applyLeaveBtn = UIComponents.secondaryButton("Apply for Leave");
        logoutBtn = UIComponents.secondaryButton("Logout");
        applyLeaveBtn.addActionListener(e -> new LeaveApplicationFrame(employee).setVisible(true));
        logoutBtn.addActionListener(new LogoutEmployeeListener());
        actionBtns.add(applyLeaveBtn);
        actionBtns.add(logoutBtn);
        panel.add(actionBtns, BorderLayout.SOUTH);
        return panel;
    }
 
    private void applySecurityVisibility() {
        boolean isSelfService = loggedInUser.getEmployeeId().equals(employee.getEmployeeId());
 
        if (isReadOnlyMode) {
            attendancePanel.setVisible(false);
            applyLeaveBtn.setVisible(false);
            if (timeInBtn != null) timeInBtn.setVisible(false);
            if (timeOutBtn != null) timeOutBtn.setVisible(false);
        } else {
            attendancePanel.setVisible(isSelfService);
            applyLeaveBtn.setVisible(isSelfService);
            if (timeInBtn != null) timeInBtn.setVisible(isSelfService);
            if (timeOutBtn != null) timeOutBtn.setVisible(isSelfService);
        }
 
        if (!isSelfService) {
            if (loggedInUser.canEditFinancials() && leaveHistoryScroll != null) {
                leaveHistoryScroll.setVisible(false);
            }
        } else {
            if (leaveHistoryScroll != null) leaveHistoryScroll.setVisible(true);
        }
 
        if (payrollSectionPanel != null) {
            if ((loggedInUser.canComputePayroll() && !isSelfService) || isSelfService) {
                payrollSectionPanel.setVisible(true);
            } else {
                payrollSectionPanel.setVisible(false);
            }
        }
 
        for (java.awt.event.ActionListener al : logoutBtn.getActionListeners()) {
            logoutBtn.removeActionListener(al);
        }
 
        if (isSelfService && !isReadOnlyMode) {
            logoutBtn.setText("Logout Account");
            logoutBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?", "Logout",
                    JOptionPane.YES_NO_OPTION);
 
                if (confirm == JOptionPane.YES_OPTION) {
                    this.dispose();
                    new LoginFrame().setVisible(true);
                }
            });
        } else {
            logoutBtn.setText("Close Window");
            logoutBtn.addActionListener(e -> dispose());
        }
    }
 
    private class ComputePayrollListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            computeBtn.setEnabled(false);
            new SwingWorker<String, Void>() {
                @Override protected String doInBackground() throws Exception {
                    return payrollProcessor.processMonthlyPayroll(
                        employee.getEmployeeId(),
                        monthComboBox.getSelectedIndex() + 1);
                }
                @Override
                protected void done() {
                    try {
                        payrollResultArea.setText(get());
                        payrollResultArea.setCaretPosition(0);
                    } catch (Exception ex) {
                        payrollResultArea.setText("Error: " + ex.getMessage());
                    } finally {
                        computeBtn.setEnabled(true);
                    }
                }
            }.execute();
        }
    }
 
    private class LogoutEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(EmployeeDetailFrame.this, "Do you want to Logout?", "Logout", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        }
    }
 
    private DefaultTableModel createReadOnlyModel(String[] columns) {
        return UIComponents.readOnlyModel(columns);
    }
 
    private void setupTableVisuals(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(22);
        table.setFillsViewportHeight(true);
        table.setFont(UITheme.FONT_BODY);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
    }
}
 