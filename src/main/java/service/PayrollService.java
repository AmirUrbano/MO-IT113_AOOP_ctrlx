/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import JasperReports.PayslipReportGenerator;
import com.mycompany.motorphpayroll_OOP.Config;
import com.mycompany.motorphpayroll_OOP.ValidationUtils;
import model.Employee;
import model.PayrollResult;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 *
 * @author Amir
 */
public class PayrollService {
    private static PayrollService instance;
    private final AttendanceService attendanceService = AttendanceService.getInstance();
    private final EmployeeService employeeDetails = EmployeeService.getInstance();
    
    private PayrollService() {}
   
    public static PayrollService getInstance() {
        if (instance == null) {
            instance = new PayrollService();
        }
        return instance;
    }

    /**
     * 
     * 
     */
    public void exportPayslipPDF(String employeeId, int month, int year, Employee currentUser) throws Exception {

        if (!employeeId.equals(currentUser.getEmployeeId()) && !currentUser.canEditFinancials()) {
            throw new SecurityException("Access Denied: You are not authorized to view or generate another employee's payslip.");
        }
        

        PayslipReportGenerator payslipGen = new PayslipReportGenerator();
        payslipGen.generateIndividualPayslip(employeeId, month, year);
    }
   
    public String processMonthlyPayroll(String employeeId, int month) {
     
        PayrollResult result = getPayrollResultForGUI(employeeId, month);
        if (result == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

        Employee employee = employeeDetails.findEmployeeById(employeeId);
        StringBuilder report = new StringBuilder();
        report.append("========================================\n");
        report.append("      MOTORPH PAYROLL REPORT\n");
        report.append("========================================\n");
        report.append(String.format("Employee ID: %s\n", employeeId));
        
 
        report.append(String.format("Name:        %s %s\n", result.firstName, result.lastName));
        report.append(String.format("Month:       %s 2024\n", getMonthName(month)));
        report.append("========================================\n");

     
        LocalDate startOfMonth = LocalDate.of(2024, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        int weekCount = 1;
        LocalDate currentWeekStart = startOfMonth.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());

        while (!currentWeekStart.isAfter(endOfMonth)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            LocalDate effectiveStart = currentWeekStart.isBefore(startOfMonth) ? startOfMonth : currentWeekStart;
            LocalDate effectiveEnd = currentWeekEnd.isAfter(endOfMonth) ? endOfMonth : currentWeekEnd;
            double totalHoursWorked = attendanceService.getHoursWorked(employeeId, effectiveStart, effectiveEnd);

            if (totalHoursWorked > 0) {
                int totalMinutesLate = attendanceService.getMinutesLate(employeeId, effectiveStart, effectiveEnd);
                int daysLate = attendanceService.getDaysLate(employeeId, effectiveStart, effectiveEnd);

                double overtimeHours = Math.max(totalHoursWorked - Config.STANDARD_WORK_HOURS, 0);
                double regularHours = Math.min(totalHoursWorked, Config.STANDARD_WORK_HOURS);
                double overtimePay = employee.calculateAdjustment(overtimeHours);
                double weeklyLateDeduction = employee.calculateAdjustment(totalMinutesLate);
                double weeklyGross = (regularHours * employee.getHourlyRate()) + overtimePay;

                report.append(getWeeklyReportString(weekCount, effectiveStart, effectiveEnd, regularHours, 
                              overtimeHours, totalHoursWorked, daysLate, totalMinutesLate, 
                              weeklyGross, weeklyLateDeduction));
                weekCount++;
            }
            currentWeekStart = currentWeekStart.plusDays(7);
        }


            report.append(getMonthlySummaryString(
            result.totalMonthlyGrossSalary, 
            result.totalLateDeductions, 
            result.sssDeduction,         
            result.philHealthDeduction,  
            result.pagIbigDeduction,     
            result.taxDeduction,
            result.totalAllowances, 
            result.totalLateDays, 
            result.totalMonthlyNetSalary
        ));
        return report.toString();
    }
    
    public PayrollResult getPayrollResultForGUI(String employeeId, int month) {
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID format: " + employeeId);
        }
        if (!ValidationUtils.isValidMonth(month)) {
            throw new IllegalArgumentException("Month must be between 1 and 12");
        }

        Employee employee = employeeDetails.findEmployeeById(employeeId);
        if (employee == null) return null;

        LocalDate startOfMonth = LocalDate.of(2024, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        
        double totalMonthlyGrossSalary = 0;
        double totalLateDeductions = 0;
        int totalLateDays = 0;

        LocalDate currentWeekStart = startOfMonth.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
        while (!currentWeekStart.isAfter(endOfMonth)) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            LocalDate effectiveStart = currentWeekStart.isBefore(startOfMonth) ? startOfMonth : currentWeekStart;
            LocalDate effectiveEnd = currentWeekEnd.isAfter(endOfMonth) ? endOfMonth : currentWeekEnd;

            double totalHoursWorked = attendanceService.getHoursWorked(employeeId, effectiveStart, effectiveEnd);
            if (totalHoursWorked > 0) {
                int totalMinutesLate = attendanceService.getMinutesLate(employeeId, effectiveStart, effectiveEnd);
                int daysLate = attendanceService.getDaysLate(employeeId, effectiveStart, effectiveEnd);

                double overtimeHours = Math.max(totalHoursWorked - Config.STANDARD_WORK_HOURS, 0);
                double regularHours = Math.min(totalHoursWorked, Config.STANDARD_WORK_HOURS);
                double overtimePay = employee.calculateAdjustment(overtimeHours);
                double weeklyLateDeduction = employee.calculateAdjustment(totalMinutesLate);
                double weeklyGross = (regularHours * employee.getHourlyRate()) + overtimePay;

                totalMonthlyGrossSalary += weeklyGross;
                totalLateDeductions += weeklyLateDeduction;
                totalLateDays += daysLate;
            }
            currentWeekStart = currentWeekStart.plusDays(7);
        }

        double totalSSS = employee.calculateSSS();
        double totalPhilHealth = employee.calculatePhilHealth();
        double totalPagIbig = employee.calculatePagIbig();
        double totalAllowances = employee.calculateBenefit();
        double totalWithholdingTax = employee.calculateWithholdingTax(totalMonthlyGrossSalary);

        double totalMonthlyNetSalary = (totalMonthlyGrossSalary - totalLateDeductions - 
                                       totalSSS - totalPhilHealth - totalPagIbig - totalWithholdingTax) + totalAllowances;

        return new PayrollResult(
            employee.getEmployeeId(),
            employee.getLastName(),
            employee.getFirstName(),
            employee.getSssNumber(),
            employee.getPhilHealth(),
            employee.getTinNumber(),
            employee.getPagIbig(),
            employee.getStatus(),
            employee.getPosition(),
            employee.getBasicSalary(),
            employee.getRiceSubsidy(),
            employee.getPhoneAllowance(),
            employee.getClothingAllowance(),
            employee.getGrossSemiMonthlyRate(),
            employee.getHourlyRate(),
            totalMonthlyGrossSalary,
            totalSSS,
            totalPhilHealth,
            totalPagIbig,
            totalWithholdingTax,
            totalLateDeductions,
            totalAllowances,
            totalMonthlyNetSalary,
            totalLateDays
        );
    }

    private String getWeeklyReportString(int week, LocalDate start, LocalDate end, double reg, double ot, 
                                        double total, int lateD, int lateM, double gross, double ded) {
        return String.format("""
                             
                             WEEK %d: %s to %s
                             -> Reg Hours: %.2f | OT Hours: %.2f
                             -> Late: %d days (%d mins)
                             -> Weekly Gross: PHP %.2f
                             -> Late Deduction: PHP %.2f
                             """, 
                week, start, end, reg, ot, lateD, lateM, gross, ded);
    }

    private String getMonthlySummaryString(double gross, double lateDed, double sss, double ph, 
                                          double pi, double tax, double allowances, int lateDays, double net) {
        return String.format("""
                             
                             ========================================
                             TOTAL MONTHLY SUMMARY:
                             -> Total Gross Salary:    PHP %.2f
                             -> Total Late Deductions: PHP %.2f
                             -> SSS Deduction:         PHP %.2f
                             -> PhilHealth Deduction:  PHP %.2f
                             -> Pag-IBIG Deduction:    PHP %.2f
                             -> Withholding Tax:       PHP %.2f
                             -> Total Allowances:      PHP %.2f (Non-Taxable)
                             -> Total Days Late:       %d
                             ----------------------------------------
                             -> TOTAL NET SALARY:      PHP %.2f
                             ========================================
                             """,
                gross, lateDed, sss, ph, pi, tax, allowances, lateDays, net);
    }

    private String getMonthName(int month) {
        return LocalDate.of(2024, month, 1).getMonth().getDisplayName(
            java.time.format.TextStyle.FULL, Locale.ENGLISH);
    }
}
