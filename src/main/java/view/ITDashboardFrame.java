/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.Employee;
import service.EmployeeService;
import service.ITService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 *
 * @author Amir
 */
public class ITDashboardFrame extends JFrame{
    private final Employee currentUser;
    private final EmployeeService employeeService;
    private final ITService itService;
    
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    
    private JTextField searchField;
    private JButton resetBtn, diagBtn, refreshBtn, logoutBtn;
    
   

    public ITDashboardFrame(Employee user, EmployeeService employeeService, ITService itService) {
        this.currentUser = user;
        this.employeeService = employeeService;
        this.itService = itService;
        setTitle("MotorPH IT Control Center - Specialist: " + user.getLastName());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        initComponents();
        setupListeners();
          
        appendLog("Session Established. Welcome, Agent " + user.getFirstName());
       
    }

    private void initComponents() {
    JPanel header = new JPanel(new BorderLayout(15, 0));
    header.setBackground(new Color(25, 25, 25));
    header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
    
    JLabel title = new JLabel("IT CONTROL CENTER");
    title.setFont(new Font("Segoe UI", Font.BOLD, 22));
    title.setForeground(Color.WHITE);
    header.add(title, BorderLayout.WEST);

 
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    searchPanel.setOpaque(false);
    searchField = new JTextField(15);
    JLabel searchLabel = new JLabel("Search ID: ");
    searchLabel.setForeground(Color.WHITE);
    searchPanel.add(searchLabel);
    searchPanel.add(searchField);
    header.add(searchPanel, BorderLayout.CENTER);

    logoutBtn = createStyledButton("Logout", new Color(150, 0, 0));
    header.add(logoutBtn, BorderLayout.EAST);
    add(header, BorderLayout.NORTH);

  
    JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
    centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    String[] columns = {"ID", "Name", "Position", "Status"};
    tableModel = createReadOnlyModel(columns);
    userTable = new JTable(tableModel);
    setupTableVisuals();
    refreshTableData();
    
    JScrollPane tableScroll = new JScrollPane(userTable);
    tableScroll.setBorder(BorderFactory.createTitledBorder("User Registry"));
    centerPanel.add(tableScroll, BorderLayout.CENTER);
    add(centerPanel, BorderLayout.CENTER);

  
    JPanel toolPanel = new JPanel(new GridLayout(6, 1, 10, 10));
    toolPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("System Actions"),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));
    toolPanel.setPreferredSize(new Dimension(220, 0));
    
     resetBtn = createStyledButton("Reset Password", new Color(0, 102, 204));
     diagBtn = createStyledButton("Run Diagnostics", new Color(60, 60, 60));
     refreshBtn = createStyledButton("Sync Registry", new Color(0, 153, 76));


    toolPanel.add(resetBtn);
    toolPanel.add(diagBtn);
    toolPanel.add(refreshBtn);
    add(toolPanel, BorderLayout.EAST);


    logArea = new JTextArea(8, 0);
    logArea.setEditable(false);
    logArea.setBackground(new Color(10, 10, 10));
    logArea.setForeground(new Color(57, 255, 20)); // the matrix vibes
    logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
    logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(BorderFactory.createTitledBorder("Security Console Output"));
    add(logScroll, BorderLayout.SOUTH);
}
    private void handleSearch(String query) {
    tableModel.setRowCount(0);
    itService.searchEmployees(query).forEach(emp -> {
        tableModel.addRow(new Object[]{
            emp.getEmployeeId(), 
            emp.getFirstName() + " " + emp.getLastName(), 
            emp.getPosition(), 
            emp.getStatus()
        });
    });
}
    private void handleReset() {
    int row = userTable.getSelectedRow();
    
    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select a user from the table first.");
        return;
    }

    String id = (String) tableModel.getValueAt(row, 0);
    String name = (String) tableModel.getValueAt(row, 1);

    int confirm = JOptionPane.showConfirmDialog(
        this, 
        "Are you sure you want to reset the password for " + name + " (ID: " + id + ")?\n" +
        "This will restore their access to the default '123'.", 
        "Confirm Security Reset", 
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE // Adds the yellow warning icon
    );
    if (confirm == JOptionPane.YES_OPTION) {
        if (itService.resetUserPassword(id)) {
            appendLog("SECURITY: Password for " + name + " [" + id + "] reset to default.");
            JOptionPane.showMessageDialog(this, "Password for " + name + " has been reset.");
        } else {
            appendLog("ERROR: Failed to reset password for ID " + id);
        }
    } else {
        appendLog("CANCELLED: Password reset for ID " + id + " aborted by IT Admin.");
    }
}

    private void refreshTableData() {
        tableModel.setRowCount(0);
        employeeService.getAllEmployees().forEach(emp -> {
            tableModel.addRow(new Object[]{emp.getEmployeeId(), emp.getFirstName() + " " + emp.getLastName(), emp.getPosition(), emp.getStatus()});
        });
    }
    
    private DefaultTableModel createReadOnlyModel(String[] columns) {
    return new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; 
        }
    };
}
    
    private void setupTableVisuals() {
       userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       userTable.getTableHeader().setReorderingAllowed(false);
       userTable.setRowHeight(25);
       userTable.setFillsViewportHeight(true);
       userTable.setShowGrid(false); 
       userTable.setIntercellSpacing(new Dimension(0, 0));
   }
    private void handleLogout() {
  
    int response = JOptionPane.showConfirmDialog(
        this, 
        "Are you sure you want to logout? Any unsaved console logs will be lost.", 
        "Confirm Logout", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE
    );

    if (response == JOptionPane.YES_OPTION) {
        appendLog("SHUTDOWN: Admin session terminated.");
        this.dispose(); 
        new LoginFrame().setVisible(true); 
    }
}
    
    
    private JButton createStyledButton(String text, Color bg) {
    JButton btn = new JButton(text);
    btn.setBackground(bg);
    btn.setForeground(Color.WHITE);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    return btn;
}
    
    private void appendLog(String msg) {
        logArea.append(itService.getTimestampedMessage(msg));
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
    
    private void setupListeners() {
    searchField.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            handleSearch(searchField.getText().trim());
        }
    });

   
    resetBtn.addActionListener(e -> handleReset());

    diagBtn.addActionListener(e -> {
        appendLog("INITIALIZING SYSTEM DIAGNOSTICS...");
        appendLog(itService.runSystemDiagnostics());
    });

    refreshBtn.addActionListener(e -> {
        refreshTableData();
        appendLog("Registry synchronized with Employee Database.");
    });

    logoutBtn.addActionListener(e -> handleLogout());
}
}
