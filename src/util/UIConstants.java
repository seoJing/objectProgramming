package util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class UIConstants {

    private UIConstants() {}

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

    // Colors - Accent Color
    public static final Color ACCENT_COLOR = new Color(52, 152, 219);

    // Colors - Background
    public static final Color BACKGROUND_LIGHT = new Color(245, 248, 250);
    public static final Color BACKGROUND_BUTTON = new Color(240, 250, 245);
    public static final Color BACKGROUND_HOVER = new Color(230, 240, 245);
    public static final Color BACKGROUND_LIST = new Color(240, 245, 250);

    // Colors - Buttons
    public static final Color BUTTON_COLOR = new Color(180, 180, 180);

    // Colors - General
    public static final Color WHITE = Color.WHITE;
    public static final Color BLACK = Color.BLACK;
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 50);
    public static final Color SHADOW_LIGHT = new Color(0, 0, 0, 8);
    public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
    public static final Color TEXT_MUTED  = new Color(120, 124, 130);
    public static final Color POS_GREEN = new Color(13, 170, 73);
    public static final Color NEG_RED = new Color(255, 0, 0);
    public static final Color PANEL_BG = WHITE;
    public static final Color TX_ROW_BG = new Color(247, 249, 252);
    public static final Color TX_ROW_BORDER_COLOR = new Color(234, 237, 242);
    public static final Color TEXT_DEFAULT = Color.BLACK;
    public static final Color DARK_GRAY_51 = new Color(51, 51, 51);
    public static final Color DARK_GRAY_170 = new Color(170, 170, 170);
    public static final Color LIGHT_GRAY_248 = new Color(248, 250, 252);
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100);
    public static final Color MUTED_GRAY_150 = new Color(150, 150, 150);
    public static final Color LIGHT_BG = new Color(240, 245, 250);
    public static final Color LIGHT_BG_HOVER = new Color(230, 240, 245);
    public static final Color VERY_LIGHT_BG = new Color(245, 248, 250);
    public static final Color ITEM_BG_LIGHT = new Color(248, 250, 252);

    // Fonts
    public static final String FONT_FAMILY = "맑은 고딕";
    public static final Font SMALL_FONT_11 = new Font(FONT_FAMILY, Font.PLAIN, 11);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font SMALL_FONT_13 = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font NORMAL_FONT_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font MEDIUM_FONT_16 = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font LARGE_FONT_18 = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font LABEL_FONT_20 = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font DISPLAY_FONT_24 = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font TITLE_FONT_40 = new Font(FONT_FAMILY, Font.BOLD, 40);
    public static final Font LARGE_FONT = new Font(FONT_FAMILY, Font.BOLD, 32);
    public static final Font HERO_FONT = new Font(FONT_FAMILY, Font.BOLD, 30);

    // Dimensions
    public static final int BUTTON_HORIZONTAL_GAP = 8;
    public static final int BUTTON_VERTICAL_GAP = 40;
    public static final int SUGGESTION_BUTTON_VERTICAL_GAP = 40;
    public static final int SCROLL_AMOUNT = 10;
    public static final int CORNER_RADIUS = 8;

    public static final int HEADER_HEIGHT = 60;
    public static final int NAV_HEIGHT = 60;

    // Component sizes
    public static final int ICON_SMALL_SIZE = 32;
    public static final int ICON_MEDIUM_SIZE = 44;
    public static final int ICON_LARGE_SIZE = 80;

    // Panel heights
    public static final int PANEL_HEIGHT_40 = 40;
    public static final int PANEL_HEIGHT_44 = 44;
    public static final int PANEL_HEIGHT_60 = 60;
    public static final int PANEL_HEIGHT_70 = 70;
    public static final int PANEL_HEIGHT_80 = 80;
    public static final int PANEL_HEIGHT_100 = 100;
    public static final int PANEL_HEIGHT_120 = 120;
    public static final int PANEL_HEIGHT_160 = 160;
    public static final int PANEL_HEIGHT_180 = 180;
    public static final int PANEL_HEIGHT_260 = 260;
    public static final int PANEL_HEIGHT_280 = 280;
    public static final int PANEL_HEIGHT_360 = 360;

    // Button sizes
    public static final Dimension BUTTON_90x30 = new Dimension(90, 30);
    public static final Dimension BUTTON_150x44 = new Dimension(150, 44);
    public static final Dimension BUTTON_80x80 = new Dimension(80, 80);
    public static final Dimension ICON_32x32 = new Dimension(32, 32);

    // Common Dimension sizes
    public static final Dimension ITEM_WIDTH_MAX = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_70);
    public static final Dimension ITEM_WIDTH_MAX_40 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_40);
    public static final Dimension ITEM_WIDTH_MAX_44 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_44);
    public static final Dimension ITEM_WIDTH_MAX_80 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_80);
    public static final Dimension ITEM_WIDTH_MAX_100 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_100);
    public static final Dimension ITEM_WIDTH_MAX_120 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_120);
    public static final Dimension ITEM_WIDTH_MAX_160 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_160);
    public static final Dimension ITEM_WIDTH_MAX_180 = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_180);
    public static final Dimension HEADER_SIZE_MAX = new Dimension(Integer.MAX_VALUE, PANEL_HEIGHT_60);
    public static final Dimension FILTER_COMBO_SIZE = new Dimension(200, ICON_MEDIUM_SIZE);
    public static final Dimension USER_COMBO_SIZE = new Dimension(150, 30);
    public static final Dimension ACCOUNT_COMBO_SMALL_SIZE = new Dimension(220, 30);
    public static final Dimension INFO_PANEL_SIZE = new Dimension(340, 0);

    // Calendar size
    public static final Dimension CALENDAR_SIZE = new Dimension(520, 360);

    public static final Dimension HEADER_SIZE = new Dimension(0, HEADER_HEIGHT);
    public static final Dimension NAV_SIZE = new Dimension(0, NAV_HEIGHT);
    public static final Dimension ACCOUNT_COMBO_SIZE = new Dimension(260, 28);

    // Borders
    public static final Border HEADER_PADDING = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    public static final Border HEADER_BOTTOM_SHADOW = BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(200, 200, 200));
    public static final Border ALERT_PANEL_PADDING_BORDER = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    public static final Border ACCOUNT_HEADER_PADDING = BorderFactory.createEmptyBorder(16, 24, 8, 24);
    public static final Border ACCOUNT_CARD_PADDING = BorderFactory.createEmptyBorder(8, 16, 25, 16);
    public static final Border ACCOUNT_BALANCE_RIGHT_PADDING = BorderFactory.createEmptyBorder(0, 12, 0, 0);
    public static final Border TOP_PANEL_BORDER = BorderFactory.createEmptyBorder(6, 8, 50, 8);
    public static final Border TOP_PANEL_CAL_BORDER = BorderFactory.createEmptyBorder(6, 8, 10, 8);
    public static final Border LIST_PANEL_PADDING = BorderFactory.createEmptyBorder(8, 12, 8, 12);
    public static final Border DAY_HEADER_PADDING = BorderFactory.createEmptyBorder(4, 12, 4, 12);
    public static final Border ICON_LEFT_PADDING = BorderFactory.createEmptyBorder(0, 0, 0, 12);
    public static final Border TX_ROW_INNER_PAD = BorderFactory.createEmptyBorder(10, 12, 10, 12);

    public static final Insets RIGHT_GAP_M = new Insets(0, 10, 0, 0);
    public static final Insets LEFT_GAP_M = new Insets(0, 0, 0, 10);
    public static final Insets TOP_GAP_M = new Insets(0, 10, 0, 0);
    public static final Insets ZERO_INSETS = new Insets(0, 0, 0, 0);

    public static final Border TX_ROW_OUTLINE = BorderFactory.createLineBorder(TX_ROW_BORDER_COLOR);
    public static final Border TX_ROW_BORDER = BorderFactory.createCompoundBorder(TX_ROW_OUTLINE, TX_ROW_INNER_PAD);

    // Window sizes
    public static final int USER_SIDE_WINDOW_WIDTH = 400;
    public static final int USER_SIDE_WINDOW_HEIGHT = 800;
    public static final int ADMIN_SIDE_WINDOW_WIDTH = 1200;
    public static final int ADMIN_SIDE_WINDOW_HEIGHT = 800;

    // Card panel style
    public static final Color CARD_BG = new Color(245, 247, 250);
    public static final Border CARD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 233, 238)),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
    );

    // Formatters
    public static final DateTimeFormatter UI_DATEFULL = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
    public static final DateTimeFormatter UI_DATE = DateTimeFormatter.ofPattern("MM.dd HH:mm");

    private static final NumberFormat KOR_NUMBER = NumberFormat.getNumberInstance(Locale.KOREA);
    public static String money(int amount) { return KOR_NUMBER.format(amount); }
    public static String won(int amount) { return KOR_NUMBER.format(amount) + "원"; }

    public static final int ACCOUNT_HEIGHT = 68;
    public static final int DAY_HEADER_HEIGHT = 32;
}
