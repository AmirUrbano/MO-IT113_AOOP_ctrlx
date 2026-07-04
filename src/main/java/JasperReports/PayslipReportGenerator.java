/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JasperReports;

import config.DatabaseConnection;
import java.sql.Connection;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Amir
 */
public class PayslipReportGenerator {

    public void generateIndividualPayslip(String employeeId, int month, int year) {
      
        try (Connection conn = DatabaseConnection.getConnection()) {
            
          
            InputStream reportStream = Thread.currentThread().getContextClassLoader()
                                       .getResourceAsStream("reports/employee_payslip.jrxml");
            if (reportStream == null) {
                throw new java.io.FileNotFoundException("Resource Error: cannot find reports/employee_payslip.jrxml");
            }

       
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
     
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("EmpIDParameter", employeeId);
            parameters.put("MonthParameter", month);
            parameters.put("YearParameter", year);

  
            InputStream logoStream = Thread.currentThread().getContextClassLoader()
                                     .getResourceAsStream("images/MotorPH_Logo.png");
            
            if (logoStream != null) {
                parameters.put("LogoParameter", logoStream);
            } else {
                System.out.println("Cannot find logo in images/ folder.");
            }

        
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            System.err.println("Payslip Generation Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
