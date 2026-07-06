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
public class ReportGenerator {
    

    public void generateReport (Connection conn, String deptFilter)
      {
        try {

            InputStream reportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("reports/payroll_summary.jrxml");

            if (reportStream == null) {
                throw new java.io.FileNotFoundException("Resource Error: cannot find  reports/payroll_summary.jrxml sa Classpath!");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            
            parameters.put("DeptParameter", deptFilter); 

            InputStream logoStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("images/MotorPH_Logo.png");
            
            if (logoStream != null) {
                parameters.put("LogoParameter", logoStream); 
            } else {
                System.out.println("Warning: cannot find logo image with still continue to report.");
            }

  
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JasperViewer.viewReport(jasperPrint, false);

             } catch (Exception e) {
                   System.out.println("Generating Report Error: " + e.getMessage());
                      e.printStackTrace();
       }
     }
}
