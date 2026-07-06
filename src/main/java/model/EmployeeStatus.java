/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * This method acts as the "Middleman".It takes the raw data array from the DB and returns the correct Child Object. 
 * @author Amir
 */

 public class EmployeeStatus {

    public static Employee createFromDb(String[] data) {
    
        String status = data[10];
        String position = data[11].toLowerCase(); 

   
        String department = data[19]; 

        try {
            double basic  = parseSafeDouble(data[13]);
            double rice   = parseSafeDouble(data[14]);
            double phone  = parseSafeDouble(data[15]);
            double cloth  = parseSafeDouble(data[16]);
            double semi   = parseSafeDouble(data[17]);
            double hourly = parseSafeDouble(data[18]);

            // Check for HR Roles
            if (position.equalsIgnoreCase("HR Manager") || position.contains("hr team leader") || position.contains("hr rank and file")) {
                return new HREmployee(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly 
                );
            } 
            // Check for Finance/Accounting/Payroll Roles
            else if (position.equalsIgnoreCase("Payroll Manager") || 
                     position.equalsIgnoreCase("Chief Finance Officer") || 
                     position.equalsIgnoreCase("Accounting Head") ||
                     position.contains("payroll") || position.contains("account")) {
          
                return new FinanceEmployee(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly
                );
            }
            // Check for Executives (Admin)
            else if (position.equalsIgnoreCase("Chief Executive Officer") || 
                     position.equalsIgnoreCase("Chief Operating Officer") ||
                     position.equalsIgnoreCase("Chief Marketing Officer")) {
                    
                return new AdminEmployee(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly 
                );
            }
            // Check for IT / Logistics Roles
            else if (position.equalsIgnoreCase("IT Operations and Systems") || position.contains("supply chain") || position.contains("logistics")) {
                return new ITEmployee(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly
                );
            }  
            // Default to Regular or Probationary based on Status
            else if ("Regular".equalsIgnoreCase(status)) {
                return new RegularEmp(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly
                );
            } else {
                return new ProbationaryEmp(
                    data[0], data[1], data[2], data[3], data[4], data[5], 
                    data[6], data[7], data[8], data[9], data[10], data[11], 
                    data[12], department, basic, rice, phone, cloth, semi, hourly 
                );
            }
        } catch (Exception e) {
            System.err.println("Error processing data for Employee ID: " + data[0] + " - " + e.getMessage());
            return null;
        }
    }
    
    private static double parseSafeDouble(String value) {
        if (value == null || value.trim().isEmpty() || 
            value.equalsIgnoreCase("None") || value.equalsIgnoreCase("N/A")) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}