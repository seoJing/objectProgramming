package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * UI constants for consistent styling across the application.
 * Centralize colors, fonts, dimensions, and other UI-related values here.
 */
public final class UIConstants {

    private UIConstants() {
        // Prevent instantiation
    }

    // Colors - Headers
    public static final Color USER_HEADER_COLOR = new Color(52, 152, 219);
    public static final Color ADMIN_HEADER_COLOR = new Color(231, 76, 60);

    // Colors - Navigation
    public static final Color NAV_BACKGROUND_COLOR = new Color(236, 240, 241);
    public static final Color NAV_HOVER_COLOR = new Color(189, 195, 199);

    // Colors - General
    public static final Color WHITE = Color.WHITE;

    // Fonts
    public static final String FONT_FAMILY = "맑은 고딕";
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font LARGE_FONT = new Font(FONT_FAMILY, Font.BOLD, 32);
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

    // Dimensions
    public static final int HEADER_HEIGHT = 60;
    public static final int NAV_HEIGHT = 60;
    public static final Dimension HEADER_SIZE = new Dimension(0, HEADER_HEIGHT);
    public static final Dimension NAV_SIZE = new Dimension(0, NAV_HEIGHT);

    // Borders
    public static final Border HEADER_PADDING = BorderFactory.createEmptyBorder(10, 20, 10, 20);

    // Application
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
}
