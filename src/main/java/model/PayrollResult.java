/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public class PayrollResult {
     public String employeeId;
   public String lastName, firstName;
    
    // Government number and Employment info
 public String sssNumber, philHealth, tinNumber, pagIbig, status, position;
    // Financials
public double basicSalary, riceSubsidy, phoneAllowance, clothingAllowance;
 public double grossSemiMonthlyRate, hourlyRate;
 
 // payroll service calculations
public double totalMonthlyGrossSalary;
    public double sssDeduction, philHealthDeduction, pagIbigDeduction, taxDeduction;
    public double totalLateDeductions;
    public double totalAllowances;
    public double totalMonthlyNetSalary; 
    public int totalLateDays;;

    public PayrollResult(String employeeId, String lastName, String firstName, String sssNumber, String philHealth, String tinNumber, String pagIbig, String status, String position, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate, double totalMonthlyGrossSalary, double sssDeduction, double philHealthDeduction, double pagIbigDeduction, double taxDeduction, double totalLateDeductions, double totalAllowances, double totalMonthlyNetSalary, int totalLateDays) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.sssNumber = sssNumber;
        this.philHealth = philHealth;
        this.tinNumber = tinNumber;
        this.pagIbig = pagIbig;
        this.status = status;
        this.position = position;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
        this.totalMonthlyGrossSalary = totalMonthlyGrossSalary;
        this.sssDeduction = sssDeduction;
        this.philHealthDeduction = philHealthDeduction;
        this.pagIbigDeduction = pagIbigDeduction;
        this.taxDeduction = taxDeduction;
        this.totalLateDeductions = totalLateDeductions;
        this.totalAllowances = totalAllowances;
        this.totalMonthlyNetSalary = totalMonthlyNetSalary;
        this.totalLateDays = totalLateDays;
    }

    
}
