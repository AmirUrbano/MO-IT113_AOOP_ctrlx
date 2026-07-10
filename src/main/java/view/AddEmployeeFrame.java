/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import view.theme.UITheme;
import view.theme.UIComponents;
import service.EmployeeService;
import javax.swing.*;
import java.awt.*;  

/**
 *
 * @author Amir
 */
public class AddEmployeeFrame extends JFrame {
    private final MainFrame parentFrame;
    private final EmployeeService employeeDetails;
    private EmployeeFormPanel formPanel;
    private JButton saveBtn, cancelBtn;
 
    public AddEmployeeFrame(MainFrame parent, EmployeeService employeeDetails) {
        this.parentFrame = parent;
        this.employeeDetails = employeeDetails;
 
        initializeGUI();
        formPanel.setEmployeeId(employeeDetails.getNextEmployeeId());
    }
 
    private void initializeGUI() {
        setTitle("MotorPH - Add New Employee");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 750);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parentFrame);
 
        // Form fields layout mapping
        formPanel = new EmployeeFormPanel();
        add(formPanel, BorderLayout.CENTER);
 
        // Control buttons panel layout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(UITheme.BG_LIGHT_GRAY);
 
        saveBtn = UIComponents.primaryButton("Save Record");
        cancelBtn = UIComponents.secondaryButton("Cancel");
 
        saveBtn.addActionListener(e -> onSave());
        cancelBtn.addActionListener(e -> dispose());
 
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
 
    private void onSave() {
        try {
 
            String[] data = formPanel.validateAndGetFormData();
 
            if (employeeDetails.registerEmployee(data)) {
                parentFrame.refreshTable();
                JOptionPane.showMessageDialog(this, "Employee successfully registered!",
                 "System Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (IllegalArgumentException ex) {
 
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
 
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}