/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


import view.theme.UITheme;
import view.theme.UIComponents;
import javax.swing.*;
import java.awt.*;
import model.Employee;
import service.LeaveService;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
/**
 *
 * @author Amir
 */
public class LeaveApplicationFrame extends JFrame {
    private final JComboBox<String> typeCombo;
    private final JDateChooser startDateChooser;
    private final JDateChooser endDateChooser;
    private final Employee currentUser;
 
    public LeaveApplicationFrame(Employee user) {
        this.currentUser = user;
        setTitle("Apply for Leave - " + user.getFirstName());
        setSize(420, 470);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
 
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, UITheme.SPACING_SM, UITheme.SPACING_SM));
        formPanel.setBackground(UITheme.BG_WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                UITheme.SPACING_LG, UITheme.SPACING_LG, UITheme.SPACING_LG, UITheme.SPACING_LG));
 
        formPanel.add(UIComponents.fieldLabel("Employee ID:"));
        formPanel.add(new JLabel(user.getEmployeeId()));
 
        formPanel.add(UIComponents.fieldLabel("Leave Type:"));
        typeCombo = new JComboBox<>(new String[]{"Vacation", "Sick Leave", "Emergency", "Maternity/Paternity"});
        formPanel.add(typeCombo);
 
        formPanel.add(UIComponents.fieldLabel("Start Date:"));
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(startDateChooser);
 
        formPanel.add(UIComponents.fieldLabel("End Date:"));
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(endDateChooser);
 
        add(formPanel, BorderLayout.CENTER);
 
        JButton submitBtn = UIComponents.primaryButton("Submit Request");
        JPanel btnWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, UITheme.SPACING_SM));
        btnWrap.setBackground(UITheme.BG_WHITE);
        btnWrap.add(submitBtn);
        submitBtn.addActionListener(e -> submitLeave());
        add(btnWrap, BorderLayout.SOUTH);
    }
 
    private void submitLeave() {
 
        if (startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please select both dates.");
            return;
        }
 
        if (endDateChooser.getDate().before(startDateChooser.getDate())) {
            JOptionPane.showMessageDialog(this,
                "Error: End Date cannot be earlier than Start Date, Please do not break the space-time continuum.",
                "Invalid Date Range", JOptionPane.ERROR_MESSAGE);
            return;
        }
 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String startStr = sdf.format(startDateChooser.getDate());
        String endStr = sdf.format(endDateChooser.getDate());
 
        java.util.Date today = new java.util.Date();
        if (startDateChooser.getDate().before(today)) {
             JOptionPane.showMessageDialog(this,
            "Invalid Date: You cannot file leave for a past date, this is not a time machine.\n" +
            "Please select a current or future date.",
            "Date Error",
            JOptionPane.ERROR_MESSAGE);
        return;
        }
 
        LeaveService.getInstance().applyForLeave(
            currentUser.getEmployeeId(),
            currentUser.getFirstName() + " " + currentUser.getLastName(),
            startStr,
            endStr,
            (String) typeCombo.getSelectedItem()
        );
 
        JOptionPane.showMessageDialog(this, "Leave Request Submitted Successfully!");
        this.dispose();
    }
}
