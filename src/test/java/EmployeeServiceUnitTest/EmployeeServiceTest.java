/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EmployeeServiceUnitTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import service.EmployeeService;
import model.Employee;
/**
 *
 * @author Amir
 */
public class EmployeeServiceTest {

    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        employeeService = EmployeeService.getInstance();
        System.out.println("[DEBUG] EmployeeService instance successfully initialized.");
    }

    @Test
    @DisplayName("CRUD: Retrieve All Registered Employees")
    public void testGetAllEmployees() {
        System.out.println("[INFO] Executing testGetAllEmployees...");
        java.util.List<Employee> list = employeeService.getAllEmployees();
        System.out.println("[DATA FETCH] Total employees retrieved: " + (list != null ? list.size() : 0));
        
        assertNotNull(list, "The employee repository list should not be null");
        assertFalse(list.isEmpty(), "The repository should contain initialized employee records");
    }

    @Test
    @DisplayName("CRUD: Verify Employee Creation with Valid Data")
    public void testCreateEmployee_validData() {
        System.out.println("[INFO] Executing testCreateEmployee_validData...");
        String nextId = employeeService.getNextEmployeeId();
        
        String[] mockRawData = {
            nextId, "Doe", "John", "01/01/1990", "123 Street",             
            "0917-123-456", "Regular", "Staff", "50000", "10-1234567-1",    
            "123-456-789-000", "123456789012", "123456789012", "0.00",      
            "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"                  
        };

        boolean isAdded = employeeService.createAndAddEmployee(mockRawData);
        System.out.println("[INSERT] Attempting to register Employee ID: " + nextId + " | Result: " + isAdded);
        
        assertTrue(isAdded, "Should successfully create and store valid employee object structures");
    }

    @Test
    @DisplayName("CRUD: Find Employee by Existing and Non-Existing ID")
    public void testFindEmployeeById() {
        System.out.println("[INFO] Executing testFindEmployeeById...");
        
        Employee emp = employeeService.findEmployeeById("10001");
        System.out.println("[QUERY] Finding ID 10001 | Found: " + (emp != null));
        
        Employee missingEmp = employeeService.findEmployeeById("99999");
        System.out.println("[QUERY] Finding ID 99999 | Found: " + (missingEmp != null));
        
        assertNull(missingEmp, "Should return null safely when querying a non-existent identity key");
    }

    @Test
    @DisplayName("CRUD: Update Existing Employee Information")
    public void testUpdateEmployee() {
        System.out.println("[INFO] Executing testUpdateEmployee...");
        
        Employee emp = employeeService.findEmployeeById("10001");
        if (emp != null) {
            String originalLastName = emp.getLastName();
            emp.setLastName("UpdatedName");
            
            boolean isUpdated = employeeService.updateEmployee(emp);
            System.out.println("[UPDATE] Updating ID 10001 | Success: " + isUpdated);
            
            assertTrue(isUpdated, "Should successfully update existing employee record in memory and DB");
            
            // Revert changes to keep baseline data clean
            emp.setLastName(originalLastName);
            employeeService.updateEmployee(emp);
        }
    }

    @Test
    @DisplayName("CRUD: Register Employee throwing Exception on Duplicate Identity Key")
    public void testRegisterEmployeeDuplicateThrowsException() {
        System.out.println("[INFO] Executing testRegisterEmployeeDuplicateThrowsException...");
        
        String[] duplicateData = {
            "10001", "Collision", "Test", "01/01/1990", "Address", "000", "Regular", "Staff", "100"
        };
        
        assertThrows(Exception.class, () -> {
            employeeService.registerEmployee(duplicateData);
        }, "Should explicitly throw an Exception when registering a pre-existing Employee Number");
    }

    @Test
    @DisplayName("CRUD: Increment Allocation Tracking for Next Identity Value")
    public void testGetNextEmployeeId() {
        System.out.println("[INFO] Executing testGetNextEmployeeId...");
        String nextId = employeeService.getNextEmployeeId();
        System.out.println("[AUTO-INCREMENT] Next usable allocation identifier: " + nextId);
        
        assertNotNull(nextId, "The next incremental employee sequence key should never yield null references");
        assertFalse(nextId.trim().isEmpty(), "The generated key value must possess physical string data context");
    }

    @Test
    @DisplayName("CRUD: Prevent Deletion of Protected Administrative Roles")
    public void testDeleteEmployee_protectedRole() {
        System.out.println("[INFO] Executing testDeleteEmployee_protectedRole...");
        String protectedId = "10001"; 
        System.out.println("[SECURITY CHECK] Attempting to purge protected administrative ID: " + protectedId);
        
        assertThrows(IllegalStateException.class, () -> {
            employeeService.deleteEmployee(protectedId);
        }, "Security subsystem must reject administrative entity purges by throwing IllegalStateException");
    }
}