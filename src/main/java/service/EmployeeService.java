/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.EmployeeDAO;
import model.Employee;
import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import model.EmployeeStatus;

public class EmployeeService {
    private static final Logger logger = Logger.getLogger(EmployeeService.class.getName());
    private final List<Employee> employeeList;
    private static EmployeeService instance;
    private final EmployeeDAO employeeDAO;

    private EmployeeService() {
      this.employeeDAO = new EmployeeDAO();
      this.employeeList = employeeDAO.load();
      
      logger.info("EmployeeService initialized with " + employeeList.size() + " database records.");
    }
    
    public static EmployeeService getInstance() {
        if (instance == null) {
            instance = new EmployeeService();
        }
        return instance;
    }
     
    
    // new methodzzz
   public Employee findEmployeeById(String employeeId) {
        return employeeList.stream()
                .filter(emp -> emp.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElse(null);
    }
   
   public boolean addEmployee(Employee employee) {
        if (findEmployeeById(employee.getEmployeeId()) != null) {
            logger.warning("Attempted to add duplicate ID: " + employee.getEmployeeId());
            return false;
        }
        employeeList.add(employee);
        employeeDAO.save(employeeList); 
        logger.info("Successfully added employee to memory and database: " + employee.getEmployeeId());
        return true;
    }
   
   public boolean createAndAddEmployee(String[] rawData) {
    Employee newEmployee = model.EmployeeStatus.createFromDb(rawData); // MIDDLEMAN returns the correct Child Object. 
    
    if (newEmployee == null) {
        logger.warning("Data transformation failed in createAndAddEmployee.");
        return false;
    }
    
    return addEmployee(newEmployee); 
}
    
   public boolean deleteEmployee(String employeeId) {
       
    Employee target = findEmployeeById(employeeId);

    if (target == null) {
        logger.warning("Delete failed: Employee ID " + employeeId + " not found.");
        return false;
    }


    if (target.isProtectedRole()) {
        logger.severe("SECURITY ALERT: Attempted to delete protected role: " + target.getPosition());
        throw new IllegalStateException("Access Denied: The position '" + target.getPosition() + "' is a protected administrative role and cannot be deleted.");
    }
   
    boolean isDeletedFromDb = employeeDAO.deleteFromDatabase(employeeId);
       
        if (isDeletedFromDb) {
            boolean removed = employeeList.remove(target);
            if (removed) {
                logger.info("Successfully deleted employee from database and memory: " + employeeId);
                return true;
            }
        }
        
        logger.warning("Failed to delete employee: " + employeeId);
        return false;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeList); 
    }
    
    public String getNextEmployeeId() {
        int maxId = employeeList.stream()
                .mapToInt(emp -> {
                    try { return Integer.parseInt(emp.getEmployeeId()); }
                    catch (NumberFormatException e) { return 0; }
                })
                .max()
                .orElse(10000);
        return String.valueOf(maxId + 1);
    }

    public boolean updateEmployee(Employee updatedEmployee) {
    for (int i = 0; i < employeeList.size(); i++) {
        if (employeeList.get(i).getEmployeeId().equals(updatedEmployee.getEmployeeId())) {
         
            employeeList.set(i, updatedEmployee);
            
          
            employeeDAO.save(employeeList); 
            
            logger.info("Successfully updated Employee ID: " + updatedEmployee.getEmployeeId());
            return true;
        }
    }
    logger.warning("Update failed: Employee ID " + updatedEmployee.getEmployeeId() + " not found.");
    return false;
}    
    

    public boolean registerEmployee(String[] data) throws Exception {

        if (findEmployeeById(data[0]) != null) {
            throw new Exception("Employee Number " + data[0] + " already exists!");
        }


        Employee employee = EmployeeStatus.createFromDb(data);
        if (employee == null) {
         throw new IllegalStateException("Unable to resolve object structure.");
     }

   
        return addEmployee(employee);
    }
}
