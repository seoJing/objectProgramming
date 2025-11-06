package main;

import javax.swing.SwingUtilities;

import util.Router;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            Router.getInstance().setMainFrame(frame);
            frame.setVisible(true);
        });
    }
}
