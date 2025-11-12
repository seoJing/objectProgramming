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
    public static final Color USER_HEADER_COLOR = new Color(11, 218, 81);
    public static final Color ADMIN_HEADER_COLOR = new Color(231, 76, 60);
    public static final Color HEADER_BACKGROUND_COLOR = new Color(240, 240, 240);

    // Colors - Navigation
    public static final Color NAV_BACKGROUND_COLOR = new Color(230, 250, 240);
    public static final Color NAV_HOVER_COLOR = new Color(200, 240, 220);

    // Colors - Text
    public static final Color TEXT_PRIMARY_COLOR = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY_COLOR = new Color(100, 100, 100);
    public static final Color TEXT_TERTIARY_COLOR = new Color(120, 120, 120);
    public static final Color TEXT_DISABLED_COLOR = new Color(150, 150, 150);

    // Colors - Background
    public static final Color BACKGROUND_LIGHT = new Color(245, 248, 250);
    public static final Color BACKGROUND_BUTTON = new Color(240, 250, 245);
    public static final Color BACKGROUND_HOVER = new Color(230, 240, 245);
    public static final Color BACKGROUND_LIST = new Color(240, 245, 250);

    // Colors - Buttons
    public static final Color BUTTON_COLOR = new Color(180, 180, 180);

    // Colors - General
    public static final Color WHITE = Color.WHITE;
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 8);

    // Fonts
    public static final String FONT_FAMILY = "맑은 고딕";
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font LARGE_FONT = new Font(FONT_FAMILY, Font.BOLD, 32);
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);

    // Dimensions - Spacing & Sizes
    public static final int BUTTON_HORIZONTAL_GAP = 8;
    public static final int BUTTON_VERTICAL_GAP = 40;
    public static final int SUGGESTION_BUTTON_VERTICAL_GAP = 40;
    public static final int SCROLL_AMOUNT = 10;
    public static final int CORNER_RADIUS = 8;

    // Dimensions - Component Heights
    public static final int HEADER_HEIGHT = 60;
    public static final int NAV_HEIGHT = 60;

    // Dimensions - Component Sizes
    public static final Dimension HEADER_SIZE = new Dimension(0, HEADER_HEIGHT);
    public static final Dimension NAV_SIZE = new Dimension(0, NAV_HEIGHT);

    // Borders
    public static final Border HEADER_PADDING = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    public static final Border HEADER_BOTTOM_SHADOW = BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(200, 200, 200));
    public static final Border ALERT_PANEL_PADDING_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    // Application - Window sizes
    public static final int USER_SIDE_WINDOW_WIDTH = 400;  // Mobile size
    public static final int USER_SIDE_WINDOW_HEIGHT = 800;
    public static final int ADMIN_SIDE_WINDOW_WIDTH = 1200;  // PC size
    public static final int ADMIN_SIDE_WINDOW_HEIGHT = 800;
}
