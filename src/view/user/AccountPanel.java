package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Account;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;

public class AccountPanel extends UserLayout {

    private JLabel totalAssetLabel;
    private JLabel ownerLabel;
    private JPanel listPanel;

    public AccountPanel() {
        super();
        setContent(createContent());
        reload();
    }

    public void reload() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        if (ownerLabel != null) {
            ownerLabel.setText(user.getName() + "님의 총 자산");
        }

        List<Account> accounts = user.getAccountList();
        int total = 0;
        for (Account a : accounts) {
            total += a.getCurrentBalance();
        }

        totalAssetLabel.setText(UIConstants.won(total));
        listPanel.removeAll();
        for (Account a : accounts) {
            listPanel.add(makeAccountCard(a));
            listPanel.add(Box.createVerticalStrut(6));
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);

        // 상단: 총 자산
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(UIConstants.ACCOUNT_HEADER_PADDING);

        User user = SessionManager.getInstance().getCurrentUser();
        String name = (user != null ? user.getName() : "");
        JLabel total = new JLabel(name + "님의 총 자산");
        total.setFont(UIConstants.NORMAL_FONT);
        total.setForeground(UIConstants.TEXT_MUTED);

        totalAssetLabel = new JLabel("0원");
        totalAssetLabel.setFont(UIConstants.HERO_FONT);
        totalAssetLabel.setForeground(Color.BLACK);

        top.add(total);
        top.add(Box.createVerticalStrut(4));
        top.add(totalAssetLabel);

        // 가운데: 계좌 카드 리스트(스크롤)
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(UIConstants.ACCOUNT_CARD_PADDING);

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // ===== 하단: 버튼들 =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 16));
        bottom.setOpaque(false);

        JButton addBtn = new JButton("계좌 추가");
        addBtn.setFont(UIConstants.NORMAL_FONT);
        addBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "계좌 추가 다이얼로그(임시)");
        });

        JButton editBtn = new JButton("계좌 수정");
        editBtn.setFont(UIConstants.NORMAL_FONT);
        editBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "계좌 수정 다이얼로그(임시)");
        });

        bottom.add(addBtn);
        bottom.add(editBtn);

        root.add(top, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        return root;
    }

    // 한 줄짜리 계좌 카드 컴포넌트
    private JComponent makeAccountCard(Account a) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(true);
        card.setBackground(UIConstants.CARD_BG);
        card.setBorder(UIConstants.CARD_BORDER);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 왼쪽: 로고 아이콘
        JLabel logo = new JLabel(loadIcon("/resources/images/credit-card.png", 24, 24));
        logo.setBorder(UIConstants.ICON_LEFT_PADDING);
        card.add(logo, BorderLayout.WEST);

        // 중앙: 은행명 + 계좌번호
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createVerticalGlue());

        JLabel bank = new JLabel(a.getBank());
        bank.setFont(UIConstants.NORMAL_FONT);
        bank.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel acc = new JLabel(a.getAccountNumber());
        acc.setFont(UIConstants.SMALL_FONT);
        acc.setForeground(UIConstants.TEXT_MUTED);

        center.add(bank);
        center.add(acc);
        center.add(Box.createVerticalGlue());
        card.add(center, BorderLayout.CENTER);

        card.setPreferredSize(new Dimension(0, UIConstants.ACCOUNT_HEIGHT));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.ACCOUNT_HEIGHT));

        // 오른쪽: 현재 잔액
        JLabel balance = new JLabel(UIConstants.won(a.getCurrentBalance()));
        balance.setFont(UIConstants.NORMAL_FONT);
        balance.setBorder(UIConstants.ACCOUNT_BALANCE_RIGHT_PADDING);
        card.add(balance, BorderLayout.EAST);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                SessionManager.getInstance().setSelectedAccount(a);
                Router.getInstance().navigateUser(Routes.TRANSACTION);
            }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(card.getBackground().darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(UIConstants.CARD_BG);
            }
        });

        return card;
    }

    private static Icon loadIcon(String path, int w, int h) {
        try {
            var url = Objects.requireNonNull(AccountPanel.class.getResource(path));
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
    }
}
