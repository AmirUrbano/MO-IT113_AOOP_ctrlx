/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AttendanceServiceUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDate;

import service.AttendanceService;
/**
 *
 * @author Amir
 */
public class AttendanceServiceTest {

    private AttendanceService attendanceService;

    @BeforeEach
    public void setUp() {
        attendanceService = AttendanceService.getInstance();
        System.out.println("[DEBUG] AttendanceService localized for metric evaluation.");
    }

    @Test
    @DisplayName("Attendance Guard: Block Chronological Date Mismatch")
    public void testValidateDateRange_invalidChronologyThrowsException() {
        System.out.println("[GUARD] Scenario: Testing start date succeeding end date constraints...");
        LocalDate start = LocalDate.of(2026, 7, 6);
        LocalDate end = LocalDate.of(2026, 7, 1); // Date sequence failure

        assertThrows(IllegalArgumentException.class, () -> {
            attendanceService.getHoursWorked("10001", start, end);
        }, "Subsystem must reject inverted date bounds by throwing IllegalArgumentException");
    }

    @Test
    @DisplayName("Attendance Metric: Aggregate Hours and Deductions Safe Fetch")
    public void testMetricsCalculationWithValidParameters() {
        System.out.println("[METRIC] Scenario: Running aggregations over valid parameters...");
        LocalDate start = LocalDate.of(2024, 9, 1);
        LocalDate end = LocalDate.of(2024, 9, 30);

        double hours = attendanceService.getHoursWorked("10001", start, end);
        int lates = attendanceService.getMinutesLate("10001", start, end);
        
        System.out.println("[DATASET] Total Hours Worked: " + hours + " | Total Late Minutes: " + lates);
        assertTrue(hours >= 0, "Hours worked calculations must evaluate to positive values");
        assertTrue(lates >= 0, "Late tracking calculation aggregations must evaluate positive");
    }

    @Test
    @DisplayName("Attendance Record: Handle Punch In and Out Lifecycle Logs")
    public void testRecordAttendanceLifecycle() {
        System.out.println("[LOG] Scenario: Simulating active punch requests...");
        String msg = attendanceService.recordAttendance("10001", true);
        
        System.out.println("[RESPONSE] System response packet message: " + msg);
        assertNotNull(msg, "The status response logging notification message block must never return null");
    }
}