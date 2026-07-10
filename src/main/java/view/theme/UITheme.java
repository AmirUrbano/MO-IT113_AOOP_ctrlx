/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.theme;


import java.awt.Color;
import java.awt.Font;
/**
 *
 * @author Amir
 */
public class UITheme {
        private UITheme() {} 
 

    public static final Color PRIMARY        = new Color(44, 62, 80);    // dark navy - headers, nav
    public static final Color PRIMARY_LIGHT  = new Color(52, 152, 219);  // blue - selections, links
    public static final Color SUCCESS        = new Color(40, 167, 69);   // green - save/approve
    public static final Color DANGER         = new Color(220, 53, 69);   // red - delete/reject
    public static final Color WARNING        = new Color(255, 193, 7);   // amber - caution states
    public static final Color INFO           = new Color(0, 102, 204);   // blue - informational actions
 

    public static final Color BG_WHITE       = Color.WHITE;
    public static final Color BG_LIGHT_GRAY  = new Color(245, 245, 245); // panel backgrounds
    public static final Color BORDER_GRAY    = new Color(230, 230, 230);
    public static final Color TEXT_PRIMARY   = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(112, 112, 112);
    public static final Color TEXT_MUTED     = Color.GRAY;
 

    private static final String FONT_FAMILY = "Segoe UI";
 
    public static final Font FONT_LOGO       = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font FONT_HEADING    = new Font(FONT_FAMILY, Font.BOLD, 22);
    public static final Font FONT_SUBHEADING = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_LABEL      = new Font(FONT_FAMILY, Font.BOLD, 12);
    public static final Font FONT_BODY       = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_BUTTON     = new Font(FONT_FAMILY, Font.BOLD, 12);
    public static final Font FONT_TABLE      = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font FONT_TABLE_HEAD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 12);
 

    public static final int SPACING_XS = 5;
    public static final int SPACING_SM = 10;
    public static final int SPACING_MD = 15;
    public static final int SPACING_LG = 20;
    public static final int SPACING_XL = 30;
 

    public static final int BUTTON_HEIGHT       = 40;
    public static final int INPUT_HEIGHT        = 32;
    public static final int TABLE_ROW_HEIGHT    = 28;
    public static final int CORNER_RADIUS       = 8; 
}

