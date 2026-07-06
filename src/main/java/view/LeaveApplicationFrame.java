/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;


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
        setSize(400, 450);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(new JLabel(user.getEmployeeId()));

        formPanel.add(new JLabel("Leave Type:"));
        typeCombo = new JComboBox<>(new String[]{"Vacation", "Sick Leave", "Emergency", "Maternity/Paternity"});
        formPanel.add(typeCombo);

        formPanel.add(new JLabel("Start Date:"));
        startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(startDateChooser);

        formPanel.add(new JLabel("End Date:"));
        endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(endDateChooser);

        add(formPanel, BorderLayout.CENTER);

      
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.addActionListener(e -> submitLeave());
        add(submitBtn, BorderLayout.SOUTH);
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
