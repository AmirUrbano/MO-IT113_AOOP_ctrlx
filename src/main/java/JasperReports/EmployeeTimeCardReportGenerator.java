/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JasperReports;

import config.DatabaseConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Amir
 */
public class EmployeeTimeCardReportGenerator {
    
        public void generateTimeCard(String empId, int month, int year) {
       
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            InputStream reportStream = Thread.currentThread().getContextClassLoader()
                                             .getResourceAsStream("reports/employee_timecard.jrxml");
            
            if (reportStream == null) {
                throw new java.io.FileNotFoundException("Resource Error: cannot find reports/employee_timecard.jrxml");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("EmpIDParameter", empId);
            parameters.put("MonthParameter", month);
            parameters.put("YearParameter", year);

            InputStream logoStream = Thread.currentThread().getContextClassLoader()
                                           .getResourceAsStream("images/MotorPH_Logo.png");
            if (logoStream != null) {
                parameters.put("LogoParameter", logoStream);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            System.err.println("Time Card Generation Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
