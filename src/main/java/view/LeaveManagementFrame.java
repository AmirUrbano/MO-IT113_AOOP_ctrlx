/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.border.EmptyBorder;
import model.LeaveRequest;
import service.LeaveService;
/**
 *
 * @author Amir
 */


public class LeaveManagementFrame extends JFrame {
    private JTable leaveTable;
    private DefaultTableModel tableModel;

    public LeaveManagementFrame() {
        initializeGUI();
        loadTableData();
    
    }

    private void initializeGUI() {
        setTitle("MotorPH | Leave Management Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));
        
        JLabel title = new JLabel("Employee Leave Requests");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(44, 62, 80));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        //table 
        String[] columnNames = {"Request ID", "Employee ID", "Name", "Type", "Start", "End", "Status"};
        tableModel = createReadOnlyModel(columnNames); 
        leaveTable = new JTable(tableModel);
        
        setupTableVisuals(leaveTable); 
        
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(new EmptyBorder(10, 25, 10, 25));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // action bar
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        actionBar.setBackground(new Color(245, 245, 245));
        actionBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        JButton approveBtn = createStyledButton("APPROVE REQUEST", new Color(40, 167, 69));
        JButton rejectBtn = createStyledButton("REJECT REQUEST", new Color(220, 53, 69));

        approveBtn.addActionListener(e -> updateLeaveStatus("Approved")); 
        rejectBtn.addActionListener(e -> updateLeaveStatus("Rejected")); 

        actionBar.add(rejectBtn);
        actionBar.add(approveBtn);
        add(actionBar, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    
    private void loadTableData() {
        tableModel.setRowCount(0);
        List<LeaveRequest> requests = LeaveService.getInstance().getAllLeaveRequests();
        for (LeaveRequest lr : requests) {
            tableModel.addRow(new Object[]{
                lr.getRequestId(), lr.getEmployeeId(), lr.getEmployeeName(),
                lr.getLeaveType(), lr.getStartDate(), lr.getEndDate(), lr.getStatus()
            });
        }
    }

   private void updateLeaveStatus(String newStatus) {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request first.");
            return;
        }

        String requestId = (String) tableModel.getValueAt(selectedRow, 0);

        boolean success = LeaveService.getInstance().updateLeaveStatus(requestId, newStatus);
        
        if (success) {
            loadTableData(); // I-refresh ang JTable mula sa database
            JOptionPane.showMessageDialog(this, "Request successfully " + newStatus + "!");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update request status in Database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
   }
   
   
  private DefaultTableModel createReadOnlyModel(String[] columns) {
    return new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
}

 private void setupTableVisuals(JTable table) {
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getTableHeader().setReorderingAllowed(false);
    table.setRowHeight(25);
    table.setFillsViewportHeight(true);
    table.setShowGrid(false); // Clean, modern look
    
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    table.getTableHeader().setBackground(new Color(44, 62, 80)); // Dark Blue Header
    table.getTableHeader().setForeground(Color.WHITE);
    table.setSelectionBackground(new Color(52, 152, 219)); // Professional Blue selection
 }  
}