/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayroll_OOP;

import static com.mysql.cj.util.TimeUtil.DATE_FORMATTER;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author Amir
 */
public class ValidationUtils {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");
    
    public static boolean isValidEmployeeId(String employeeId) {
        return employeeId != null && employeeId.matches("\\d{5}");
    }
    
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }
    
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isValidTime(String timeStr) {
    if (timeStr == null) return false;
    try {
        java.time.LocalTime.parse(timeStr.trim(), TIME_FORMATTER);
        return true;
    } catch (Exception e) {
        return false;
    }
   }
    
   public static boolean isValidSSS(String sss) {
        return sss != null && sss.matches("\\d{2}-\\d{7}-\\d{1}");
    }

    public static boolean isValidTIN(String tin) {
        return tin != null && tin.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{3}-\\d{3}-\\d{3}");
    }

    public static boolean isValidPagIbig(String pi) {
        return pi != null && pi.matches("\\d{12}");
    }

    public static boolean isValidPhilHealth(String ph) {
        return ph != null && ph.matches("\\d{12}");
    }

    public static void validateAge(String bdayStr) throws IllegalArgumentException {
        try {
            LocalDate birthDate = LocalDate.parse(bdayStr, DATE_FORMATTER);
            if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
                throw new IllegalArgumentException("Employee must be at least 18 years old.");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Birthday format must be MM/dd/yyyy.");
        }
    } 
}   
    