/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public class ProbationaryEmp extends Employee {

    public ProbationaryEmp(String employeeId, String lastName, String firstName, String birthday, String address, String phoneNumber, String sssNumber, String philHealth, String tinNumber, String pagIbig, String status, String position, String supervisor, double basicSalary, double riceSubsidy, double phoneAllowance, double clothingAllowance, double grossSemiMonthlyRate, double hourlyRate) {
        super(employeeId, lastName, firstName, birthday, address, phoneNumber, sssNumber, philHealth, tinNumber, pagIbig, status, position, supervisor, basicSalary, riceSubsidy, phoneAllowance, clothingAllowance, grossSemiMonthlyRate, hourlyRate);
    }
    
    @Override
    public double calculateSSS() { return super.calculateSSS(); }

    @Override
    public double calculatePhilHealth() { return super.calculatePhilHealth(); }

    @Override
    public double calculatePagIbig() { return super.calculatePagIbig(); }

    @Override
    public double calculateGrossSalary(double hoursWorked) { return super.calculateGrossSalary(hoursWorked); }

    @Override
    public double calculateWithholdingTax(double grossSalary) {return super.calculateWithholdingTax(grossSalary); }
    @Override
    public boolean canFileLeave() {return false; }

    @Override
    public double calculateAdjustment(double overtimeHours) {
    return super.calculateAdjustment(overtimeHours); }
    @Override
    public double calculateAdjustment(int totalMinutesLate) {
    return super.calculateAdjustment(totalMinutesLate);}
    
    @Override
    public boolean canViewDatabase() {return false;}
    
    @Override public boolean canComputePayroll() {return true;}
}
