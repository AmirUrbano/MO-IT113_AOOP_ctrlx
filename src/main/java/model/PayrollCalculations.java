/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public interface PayrollCalculations {
    
    //montly and semi monthly salary
    double calculateGrossSalary(double hoursWorked);
    
    // Overtime calculation and  // Attendance based deduction Overload 
    double calculateAdjustment(double hours); 
    double calculateAdjustment(int minutes);
    
    // government deductions
    double calculateSSS();
    double calculatePhilHealth();
    double calculatePagIbig();
    
    // tax calculations 
    double calculateWithholdingTax(double taxableIncome);
    
    
}
