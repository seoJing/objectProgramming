package view.admin;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.layout.AdminLayout;

public class AdminMainPanel extends AdminLayout {

    public AdminMainPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new GridBagLayout());

        // 기존 HELLO 라벨
        JLabel label = new JLabel("HELLO ADMIN_MAIN");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        // 간단하고 빠르게: 대시보드로 이동 버튼 추가
        JButton dashboardBtn = new JButton("대시보드로 이동");
        dashboardBtn.setFont(UIConstants.NORMAL_FONT);

        // 버튼 클릭 → 관리자 대시보드 화면으로 이동
        dashboardBtn.addActionListener(e -> {
            Router.getInstance().navigateAdmin(Routes.ADMIN_DASHBOARD);
        });

        panel.add(dashboardBtn);

        return panel;
    }
}
