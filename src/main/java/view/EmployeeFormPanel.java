/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import view.theme.UITheme;
import com.mycompany.motorphpayroll_OOP.ValidationUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import model.Employee;
import service.EmployeeService;
/**
 *
 * @author Amir
 */
public class EmployeeFormPanel extends JPanel {
 
    private JTextField lastNameField, firstNameField, addressField, phoneField;
    private JTextField sssField, philHealthField, tinField, pagIbigField;
    private JTextField employeeIdField, birthdayField, supervisorField;
    private JTextField basicSalaryField, riceSubsidyField, phoneAllowanceField, clothingAllowanceField, semiMonthlyField, hourlyRateField;
    private JComboBox<String> statusField;
    private JComboBox<String> positionField;
 
    public EmployeeFormPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(UITheme.BG_WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
        initComponents();
    }
 
    private void initComponents() {
 
        JPanel formGrid = new JPanel(new GridLayout(0, 2, 20, 12));
        formGrid.setBackground(UITheme.BG_WHITE);
 
        // Personal deets
        addField(formGrid, "Employee ID:", employeeIdField = new JTextField());
        addField(formGrid, "First Name:", firstNameField = new JTextField());
        addField(formGrid, "Last Name:", lastNameField = new JTextField());
 
        addField(formGrid, "Birthday:", birthdayField = new JTextField());
 
        addField(formGrid, "Phone Number:", phoneField = new JTextField());
        addField(formGrid, "Address:", addressField = new JTextField());
 
        // Government IDs
        addField(formGrid, "SSS #:", sssField = new JTextField());
        addField(formGrid, "PhilHealth #:", philHealthField = new JTextField());
        addField(formGrid, "TIN #:", tinField = new JTextField());
        addField(formGrid, "Pag-IBIG #:", pagIbigField = new JTextField());
 
        // Employment
        String[] statuses = {"Regular", "Probationary"};
        statusField = new JComboBox<>(statuses);
        addField(formGrid, "Status:", statusField);
 
        String[] positions = {
               "Chief Executive Officer",
               "Chief Operating Officer",
               "Chief Finance Officer",
               "Chief Marketing Officer",
               "IT Operations and Systems",
                "HR Manager",
               "HR Team Leader",
                "HR Rank and File",
                "Accounting Head",
               "Payroll Manager",
               "Payroll Team Leader",
                "Payroll Rank and File",
                "Account Manager",
               "Account Team Leader",
                "Account Rank and File",
               "Sales & Marketing",
                "Supply Chain and Logistics",
               "Customer Service and Relations"
        };
        positionField = new JComboBox<>(positions);
        addField(formGrid, "Position:", positionField);
 
        addField(formGrid, "Immediate Supervisor:", supervisorField = new JTextField());
 
        //Financials
        addField(formGrid, "Basic Salary:", basicSalaryField = new JTextField());
        addField(formGrid, "Rice Subsidy:", riceSubsidyField = new JTextField());
        addField(formGrid, "Phone Allowance:", phoneAllowanceField = new JTextField());
        addField(formGrid, "Clothing Allowance:", clothingAllowanceField = new JTextField());
        addField(formGrid, "Gross Semi-Monthly:", semiMonthlyField = new JTextField());
        addField(formGrid, "Hourly Rate:", hourlyRateField = new JTextField());
 
        JScrollPane scrollPane = new JScrollPane(formGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smoother scrolling
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
 
    private void addField(JPanel container, String labelText, JComponent component) {
        JPanel p = new JPanel(new BorderLayout(5, 2));
        p.setBackground(UITheme.BG_WHITE);
 
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        component.setPreferredSize(new Dimension(component.getPreferredSize().width, UITheme.INPUT_HEIGHT - 2));
 
        p.add(lbl, BorderLayout.NORTH);
        p.add(component, BorderLayout.CENTER);
        container.add(p);
    }
 
    public void setEmployeeData(Employee employee) {
        employeeIdField.setText(employee.getEmployeeId());
        lastNameField.setText(employee.getLastName());
        firstNameField.setText(employee.getFirstName());
        birthdayField.setText(employee.getBirthday());
        addressField.setText(employee.getAddress());
        phoneField.setText(employee.getPhoneNumber());
        sssField.setText(employee.getSssNumber());
        philHealthField.setText(employee.getPhilHealth());
        tinField.setText(employee.getTinNumber());
        pagIbigField.setText(employee.getPagIbig());
 
        statusField.setSelectedItem(employee.getStatus());
        positionField.setSelectedItem(employee.getPosition());
 
        supervisorField.setText(employee.getSupervisor());
        basicSalaryField.setText(String.valueOf(employee.getBasicSalary()));
        riceSubsidyField.setText(String.valueOf(employee.getRiceSubsidy()));
        phoneAllowanceField.setText(String.valueOf(employee.getPhoneAllowance()));
        clothingAllowanceField.setText(String.valueOf(employee.getClothingAllowance()));
        semiMonthlyField.setText(String.valueOf(employee.getGrossSemiMonthlyRate()));
        hourlyRateField.setText(String.valueOf(employee.getHourlyRate()));
    }
 
    public String[] getFormData() {
 
        return new String[] {
            employeeIdField.getText().trim(),
            lastNameField.getText().trim(),
            firstNameField.getText().trim(),
            birthdayField.getText().trim(),
            addressField.getText().trim(),
            phoneField.getText().trim(),
            sssField.getText().trim(),
            philHealthField.getText().trim(),
            tinField.getText().trim(),
            pagIbigField.getText().trim(),
            statusField.getSelectedItem().toString(),   // From Dropdown
            positionField.getSelectedItem().toString(), // From Dropdown
            supervisorField.getText().trim(),
            basicSalaryField.getText().trim(),
            riceSubsidyField.getText().trim(),
            phoneAllowanceField.getText().trim(),
            clothingAllowanceField.getText().trim(),
            semiMonthlyField.getText().trim(),
            hourlyRateField.getText().trim()
        };
    }
 
    public void setFieldsEditable(boolean canEditBasicInfo, boolean canEditFinancials) {
        // Basic Info Group
        employeeIdField.setEditable(false);
        employeeIdField.setFocusable(false);
        lastNameField.setEditable(canEditBasicInfo);
        firstNameField.setEditable(canEditBasicInfo);
        birthdayField.setEditable(canEditBasicInfo);
        addressField.setEditable(canEditBasicInfo);
        phoneField.setEditable(canEditBasicInfo);
        sssField.setEditable(canEditBasicInfo);
        philHealthField.setEditable(canEditBasicInfo);
        tinField.setEditable(canEditBasicInfo);
        pagIbigField.setEditable(canEditBasicInfo);
        statusField.setEnabled(canEditBasicInfo);
        positionField.setEnabled(canEditBasicInfo);
        supervisorField.setEditable(canEditBasicInfo);
 
        // Financial Group
        basicSalaryField.setEditable(canEditFinancials);
        riceSubsidyField.setEditable(canEditFinancials);
        phoneAllowanceField.setEditable(canEditFinancials);
        clothingAllowanceField.setEditable(canEditFinancials);
        semiMonthlyField.setEditable(canEditFinancials);
        hourlyRateField.setEditable(canEditFinancials);
    }
 
    public void clearForm() {
 
        JTextField[] fields = {
            lastNameField, firstNameField, addressField, phoneField,birthdayField,
            sssField, philHealthField, tinField, pagIbigField, employeeIdField,
            supervisorField, basicSalaryField, riceSubsidyField,
            phoneAllowanceField, clothingAllowanceField,
            semiMonthlyField, hourlyRateField
        };
        for (JTextField f : fields) { f.setText(""); }
 
        statusField.setSelectedIndex(0);
        positionField.setSelectedIndex(0);
    }
 
    public String[] validateAndGetFormData() throws IllegalArgumentException {
        String[] data = getFormData();
        String[] labels = {"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                           "SSS #", "PhilHealth #", "TIN #", "Pag-ibig #", "Status", "Position",
                           "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                           "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"};
 
        for (int i = 0; i < data.length; i++) {
            if (data[i].trim().isEmpty()) throw new IllegalArgumentException(labels[i] + " is required.");
        }
 
        for (int i = 13; i <= 18; i++) {
            try { if (Double.parseDouble(data[i]) < 0) throw new IllegalArgumentException(labels[i] + " must be positive."); }
            catch (NumberFormatException e) { throw new IllegalArgumentException(labels[i] + " must be a number."); }
        }
 
        if (!ValidationUtils.isValidEmployeeId(data[0])) throw new IllegalArgumentException("Employee # must be 5 digits.");
        ValidationUtils.validateAge(data[3]);
        if (!ValidationUtils.isValidPhone(data[5])) throw new IllegalArgumentException("Phone format: 000-000-000");
        if (!ValidationUtils.isValidSSS(data[6])) throw new IllegalArgumentException("SSS format: 00-0000000-0");
        if (!ValidationUtils.isValidPhilHealth(data[7])) throw new IllegalArgumentException("PhilHealth must be 12 digits.");
        if (!ValidationUtils.isValidTIN(data[8])) throw new IllegalArgumentException("TIN format: 000-000-000-000");
        if (!ValidationUtils.isValidPagIbig(data[9])) throw new IllegalArgumentException("Pag-ibig must be 12 digits.");
 
        return data;
    }
 
    public Employee getEmployeeFromFields() {
        String[] d = getFormData();
        EmployeeService service = EmployeeService.getInstance();
        return service.findEmployeeById(d[0]);
    }
 
    public void setEmployeeId(String id) {
        employeeIdField.setText(id);
    }
}
