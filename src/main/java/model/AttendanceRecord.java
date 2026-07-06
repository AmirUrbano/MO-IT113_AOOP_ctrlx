/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


/**
 *
 * @author Amir
 */

import com.mycompany.motorphpayroll_OOP.Config;
import com.mycompany.motorphpayroll_OOP.ValidationUtils;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

public class AttendanceRecord {
    private String employeeId;
    private String date;
    private LocalTime logIn;
    private LocalTime logOut;
    private boolean isLate;
    private int lateMinutes;
    private double hoursWorked;
    private int weekNumber;

   
  //  private static final DateTimeFormatter CSV_DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
  //  private static final DateTimeFormatter INTERNAL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

   
    public AttendanceRecord(String employeeId, String date, LocalTime logIn, LocalTime logOut,
                             boolean isLate, int lateMinutes, double hoursWorked, int weekNumber) {
        this.employeeId = employeeId;
        this.date = date;
        this.logIn = logIn;
        this.logOut = logOut;
        this.isLate = isLate;
        this.lateMinutes = lateMinutes;
        this.hoursWorked = hoursWorked;
        this.weekNumber = weekNumber;
    }

  
    public AttendanceRecord(String employeeId, String date, String logIn, String logOut) {
     
        if (!ValidationUtils.isValidEmployeeId(employeeId)) {
            throw new IllegalArgumentException("Invalid employee ID: " + employeeId);
        }
        if (!ValidationUtils.isValidTime(logIn)) {
            throw new IllegalArgumentException("Invalid login time: " + logIn);
        }
        if (!ValidationUtils.isValidTime(logOut)) {
            throw new IllegalArgumentException("Invalid logout time: " + logOut);
        }
        
        this.employeeId = employeeId;
        this.date = date;

      
        this.logIn = LocalTime.parse(logIn.trim(), TIME_FORMATTER);
        this.logOut = LocalTime.parse(logOut.trim(), TIME_FORMATTER);
        
        calculateLateAndHours();
    }

   
    private void calculateLateAndHours() {
    this.isLate = logIn.isAfter(Config.EXPECTED_LOGIN);
    this.lateMinutes = isLate ? (int) Duration.between(Config.EXPECTED_LOGIN, logIn).toMinutes() : 0;

    long totalMinutes = Duration.between(logIn, logOut).toMinutes();
    
    //  - 1hr for unpaid lunch if worked more than 5 hours
    if (totalMinutes > 300) {
        totalMinutes -= 60;
    }
    
    this.hoursWorked = Math.max(0, totalMinutes / 60.0);
}

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getDate() { return date; }
    public LocalTime getLogInTime() { return logIn; }
    public LocalTime getLogOutTime() { return logOut; }
    public boolean isLate() { return isLate; }
    public int getLateMinutes() { return lateMinutes; }
    public double getHoursWorked() { return hoursWorked; }
    public int getWeekNumber() { return weekNumber; }

    @Override
    public String toString() {
        return String.format("ID: %s | Date: %s | In: %s | Out: %s | Late: %s | Hours: %.2f",
                employeeId, date, logIn, logOut, (isLate ? lateMinutes + "m" : "No"), hoursWorked);
    }
}
