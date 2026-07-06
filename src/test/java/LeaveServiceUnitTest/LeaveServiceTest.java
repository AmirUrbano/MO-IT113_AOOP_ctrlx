/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LeaveServiceUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import service.LeaveService;
import model.LeaveRequest;
/**
 *
 * @author Amir
 */
public class LeaveServiceTest {

    private LeaveService leaveService;

    @BeforeEach
    public void setUp() {
        leaveService = LeaveService.getInstance();
        System.out.println("[DEBUG] LeaveService engine localized for auditing parameters.");
    }

    @Test
    @DisplayName("Leave Math: Audit Date Duration Day Counter Accuracy")
    public void testCalculateLeaveDays_verifyInclusiveComputations() {
        System.out.println("[LEAVE-MATH] Scenario: Auditing inclusive day counter duration logic...");
        long days = leaveService.calculateLeaveDays("2026-07-01", "2026-07-05");
        
        System.out.println("[COMPUTATION] Total calendar span distance: " + days + " days");
        assertEquals(5, days, "Inclusive day cardinality equations must compute exactly to 5 days");
    }

    @Test
    @DisplayName("Leave State: Track Employee History Records Query")
    public void testGetLeaveHistoryForEmployee() {
        System.out.println("[LEAVE-QUERY] Scenario: Extracting leave requests history records by employee key...");
        List<LeaveRequest> list = leaveService.getLeaveHistoryForEmployee("10001");
        
        assertNotNull(list, "Query result wrapper blocks must yield active non-null container elements");
    }

    @Test
    @DisplayName("Leave Operations: Intercept Status Updates Over Missing Requests")
    public void testUpdateLeaveStatus_returnsFalseForInvalidRequestId() {
        System.out.println("[LEAVE-STATE] Scenario: Testing status overrides against non-existent requests...");
        boolean success = leaveService.updateLeaveStatus("LR-EMPTY999", "Approved");
        
        assertFalse(success, "Status processing pipelines must return false cleanly when query targets do not match state");
    }
}
