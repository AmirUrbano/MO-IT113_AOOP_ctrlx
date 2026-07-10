/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import view.theme.UITheme;
import view.theme.UIComponents;
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
        header.setBackground(UITheme.BG_WHITE);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));
 
        JLabel title = UIComponents.heading("Employee Leave Requests");
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);
 
        //table
        String[] columnNames = {"Request ID", "Employee ID", "Name", "Type", "Start", "End", "Status"};
        leaveTable = UIComponents.readOnlyTable(columnNames);
        tableModel = (DefaultTableModel) leaveTable.getModel();
 
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        scrollPane.setBorder(new EmptyBorder(10, 25, 10, 25));
        scrollPane.getViewport().setBackground(UITheme.BG_WHITE);
        add(scrollPane, BorderLayout.CENTER);
 
        // action bar
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        actionBar.setBackground(UITheme.BG_LIGHT_GRAY);
        actionBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
 
        JButton approveBtn = UIComponents.primaryButton("APPROVE REQUEST");
        JButton rejectBtn = UIComponents.dangerButton("REJECT REQUEST");
 
        approveBtn.addActionListener(e -> updateLeaveStatus("Approved"));
        rejectBtn.addActionListener(e -> updateLeaveStatus("Rejected"));
 
        actionBar.add(rejectBtn);
        actionBar.add(approveBtn);
        add(actionBar, BorderLayout.SOUTH);
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
}