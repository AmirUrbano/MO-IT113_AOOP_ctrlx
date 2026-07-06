/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorphpayroll_OOP;

import view.LoginFrame;
import javax.swing.*;
/**
 *
 * @author Amir
 */
public class MotorPHEmployeeApp {
    public static void main(String[] args) {
       SwingUtilities.invokeLater(() -> {
       new LoginFrame().setVisible(true);  
});
    }
}