/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package model;

/**
 *
 * @author Amir
 */
public interface EmployeePermissions {
    // General Viewing
    boolean canViewDatabase();      // Can see the list (Admin, HR, Finance)
    boolean canViewAllRecords();    // Privacy check
    
    // HR Specifics
    boolean canAddEmployee();    // Add/Create Employees
    boolean canEditBasicInfo();     // Edit Address, Phone, etc.
    boolean canDeleteEmployee(); // Delete Employee
    
    // Finance Specifics
    boolean canEditFinancials();    // Edit Salary, Rice Subsidy, Allowances
    boolean canComputePayroll();
    // Leave Management
    boolean canFileLeave();
    boolean canApproveLeave();
    
    //IT specifics
    boolean canAccessSystemTools();
    
    // protected roles
    boolean isProtectedRole();
    
}
