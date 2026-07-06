/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ITServiceUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;

import service.ITService;
import model.Employee;
/**
 *
 * @author Amir
 */
  public class ITServiceTest {

    private ITService itService;

    @BeforeEach
    public void setUp() {
        itService = ITService.getInstance();
        System.out.println("[DEBUG] ITService utilities loaded for system checks.");
    }

    @Test
    @DisplayName("IT Admin: Verify Employee Pattern Query Search Routing")
    public void testSearchEmployees_validAndEmptyQueries() {
        System.out.println("[IT-QUERY] Scenario: Testing dynamic employee record filtering...");
        
        List<Employee> blankSearch = itService.searchEmployees("");
        List<Employee> querySearch = itService.searchEmployees("John");

        assertNotNull(blankSearch, "Empty text fields must yield the full master record list safely");
        assertNotNull(querySearch, "Pattern filtering queries should return a structured list");
    }

    @Test
    @DisplayName("IT Diagnostics: Audit Infrastructure Diagnostic Document Layout")
    public void testRunSystemDiagnostics_verifiesReportOutput() {
        System.out.println("[DIAGNOSTICS] Scenario: Running environment structural layout checks...");
        String report = itService.runSystemDiagnostics();
        
        assertNotNull(report, "The infrastructure compilation string block cannot return null references");
        assertTrue(report.contains("SYSTEM DIAGNOSTICS REPORT"), "Output summary file layout must contain header metadata tokens");
    }

    @Test
    @DisplayName("IT Security: Trigger Safe Administrative Password Reset")
    public void testResetUserPassword() {
        System.out.println("[SECURITY] Scenario: Triggering administrative bypass reset overrides...");
        boolean status = itService.resetUserPassword("10001");
        
        assertTrue(status, "The execution bridge trigger status flag must evaluate to true upon dispatch");
    }
}

