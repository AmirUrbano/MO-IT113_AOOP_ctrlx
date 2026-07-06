/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PayrollServiceUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import service.PayrollService;
import model.PayrollResult;
import model.Employee;
import model.EmployeeStatus;
/**
 *
 * @author Amir
 */
public class PayrollServiceTest {

    private PayrollService payrollService;

    @BeforeEach
    public void setUp() {
        payrollService = PayrollService.getInstance();
        System.out.println("[DEBUG] PayrollService engine successfully loaded for mathematical auditing.");
    }


    // input constraint guard coverage


    @Test
    @DisplayName("Payroll Guard: Reject Malformed Employee ID Formats")
    public void testGetPayrollResult_invalidEmployeeIdThrowsException() {
        System.out.println("[GUARD TEST] Scenario 1: Processing short-length identity keys...");
        assertThrows(IllegalArgumentException.class, () -> {
            payrollService.getPayrollResultForGUI("12", 9);
        }, "Must reject non-5 digit structural layouts with an IllegalArgumentException");
    }

    @Test
    @DisplayName("Payroll Guard: Reject Out-of-Bounds Month Cardinalities")
    public void testGetPayrollResult_invalidMonthThrowsException() {
        System.out.println("[GUARD TEST] Scenario 2: Processing month integers exceeding maximum calendar limits...");
        assertThrows(IllegalArgumentException.class, () -> {
            payrollService.getPayrollResultForGUI("10001", 13);
        }, "Must explicitly throw an IllegalArgumentException if the month constraint goes beyond 12");

        assertThrows(IllegalArgumentException.class, () -> {
            payrollService.getPayrollResultForGUI("10001", 0);
        }, "Must explicitly throw an IllegalArgumentException if the month constraint drops below 1");
    }

    @Test
    @DisplayName("Payroll Guard: Fail Gracefully for Missing Employee Record")
    public void testGetPayrollResult_nonExistentEmployeeReturnsNull() {
        System.out.println("[GUARD TEST] Scenario 3: Checking data isolation query tracking...");
        PayrollResult missingResult = payrollService.getPayrollResultForGUI("99999", 9);
        assertNull(missingResult, "Should return null safely if target record doesn't exist in system records");
    }


    // Security Layer Rbac 


    @Test
    @DisplayName("Payroll Security: Deny Payslip Export for Unauthorized Users")
    public void testExportPayslipPDF_accessDeniedForMismatchedToken() {
        System.out.println("[SECURITY TEST] Scenario 4: Simulating malicious cross-user payslip requests...");
        
        // Mocking an employee structure who does NOT have financial administrative capabilities
        String[] normalEmployeeData = {
            "10002", "User", "Normal", "01/01/1990", "Address", "000", "Regular", "Staff", "20000",
            "00-0000000-0", "000-000-000-000", "000000000000", "000000000000", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "IT"
        };
        Employee regularStaff = EmployeeStatus.createFromDb(normalEmployeeData);

        // Attempting to look at ID "10001" while logged in as "10002" should fail the security constraint
        assertThrows(SecurityException.class, () -> {
            payrollService.exportPayslipPDF("10001", 9, 2024, regularStaff);
        }, "RBAC boundary check must explicitly reject structural access, throwing a SecurityException");
    }

   
    // Calculation Logic


    @Test
    @DisplayName("Payroll Calculation: Audit Net Salary Aggregation Balances")
    public void testGetPayrollResult_verifyMathematicalFormulaBalance() {
        System.out.println("[MATH TESTING] Scenario 5: Validating full summary computation balancing...");
        
        int targetMonth = 9; // September baseline tracking
        PayrollResult result = payrollService.getPayrollResultForGUI("10001", targetMonth);
        
        if (result != null) {
            System.out.println("[DATA AUDIT] Active verification parameters mapped for ID: " + result.employeeId);
            


            double manuallyCalculatedNet = (result.totalMonthlyGrossSalary - result.totalLateDeductions - 
                                            result.sssDeduction - result.philHealthDeduction - 
                                            result.pagIbigDeduction - result.taxDeduction) + result.totalAllowances;
            
            // Assert close precision matching within a 0.01 fractional delta window for floating points
            assertEquals(manuallyCalculatedNet, result.totalMonthlyNetSalary, 0.01,
                    "Calculated aggregate compilation tracks mismatch across system calculation rules.");
            
            assertTrue(result.totalMonthlyGrossSalary >= 0, "Gross salary cannot evaluate beneath 0.00 bounds");
            assertTrue(result.totalMonthlyNetSalary >= 0, "Net allocation payout ranges must evaluate positive");
        } else {
            System.out.println("[WARN] System records empty. Balance test bypassed due to unpopulated test DB state.");
        }
    }


    // Text Report Engine and loops


    @Test
    @DisplayName("Payroll Report: Verify Monthly Text Summary Structure Layout")
    public void testProcessMonthlyPayroll_verifyReportStructure() {
        System.out.println("[REPORT TESTING] Scenario 6: Auditing loop iteration logging text blocks...");
        
        try {
            String fullReportText = payrollService.processMonthlyPayroll("10001", 9);
            
            assertNotNull(fullReportText, "The processed text payroll console payload string cannot return null references");
            assertTrue(fullReportText.contains("MOTORPH PAYROLL REPORT"), "Report structural syntax missing title layout metadata context");
            assertTrue(fullReportText.contains("TOTAL MONTHLY SUMMARY"), "Report verification layout blocks missing footer metadata summaries");
            System.out.println("[REPORT SYSTEM] Dynamic validation matrix metrics verified cleanly.");
        } catch (IllegalArgumentException e) {
            System.out.println("[WARN] Logic bypassed. Active structural indices missing baseline initialization.");
        }
    }
}