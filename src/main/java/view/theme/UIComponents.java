/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
/**
 *
 * @author Amir
 */
public class UIComponents {
    private UIComponents() {}
 

 
    public static JButton primaryButton(String text) {
        return styledButton(text, UITheme.SUCCESS);
    }
 
    public static JButton dangerButton(String text) {
        return styledButton(text, UITheme.DANGER);
    }
 
    public static JButton infoButton(String text) {
        return styledButton(text, UITheme.INFO);
    }
 
    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
 

    public static JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(
                Math.max(140, btn.getPreferredSize().width), UITheme.BUTTON_HEIGHT));
        return btn;
    }
 

 

    public static DefaultTableModel readOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
 

    public static JTable readOnlyTable(String[] columns) {
        JTable table = new JTable(readOnlyModel(columns));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(UITheme.TABLE_ROW_HEIGHT);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(UITheme.FONT_TABLE);
        table.getTableHeader().setFont(UITheme.FONT_TABLE_HEAD);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(UITheme.PRIMARY_LIGHT);
        table.setSelectionForeground(Color.WHITE);
        return table;
    }
 

 
    public static JLabel heading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_HEADING);
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        return lbl;
    }
 
    public static JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UITheme.FONT_LABEL);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        return lbl;
    }
 

    public static JPanel card(int padding) {
        JPanel panel = new JPanel();
        panel.setBackground(UITheme.BG_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)
        ));
        return panel;
    }
 
    /** Standard header bar used across dashboards (dark background, white title). */
    public static JPanel headerBar(String title) {
        JPanel header = new JPanel(new BorderLayout(UITheme.SPACING_MD, 0));
        header.setBackground(UITheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(
                UITheme.SPACING_MD, UITheme.SPACING_LG, UITheme.SPACING_MD, UITheme.SPACING_LG));
 
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.FONT_HEADING);
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        return header;
    }
}

