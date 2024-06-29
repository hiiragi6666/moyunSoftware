package UI;

import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MainUI {
    private JFrame frame;
    private JPanel mainPanel;
    private BookLibraryPanel bookLibraryPanel;
    private JPanel friendPanel;
    private PersonalInfoPanel personalInfoPanel;
    private CirclePanel circlePanel;
    private CardLayout cardLayout;
    private User user;

    public MainUI(User user) throws SQLException {
        this.user = user;

        frame = new JFrame("主界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        bookLibraryPanel = new BookLibraryPanel(user);
        mainPanel.add(bookLibraryPanel, "书库");

        friendPanel = new FriendManagementPanel(user);
        mainPanel.add(friendPanel, "好友");

        personalInfoPanel = new PersonalInfoPanel(user);
        mainPanel.add(personalInfoPanel, "个人信息");

        circlePanel = new CirclePanel(user);
        mainPanel.add(circlePanel, "圈子");

        JPanel buttonPanel = new JPanel();
        JButton bookLibraryButton = new JButton("书库");
        JButton friendButton = new JButton("好友");
        JButton personalInfoButton = new JButton("个人信息");
        JButton circleButton = new JButton("圈子");
        JButton logoutButton = new JButton("注销");

        buttonPanel.add(bookLibraryButton);
        buttonPanel.add(friendButton);
        buttonPanel.add(personalInfoButton);
        buttonPanel.add(circleButton);
        buttonPanel.add(logoutButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        bookLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "书库");
            }
        });

        friendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "好友");
            }
        });

        personalInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "个人信息");
            }
        });

        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "圈子");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new LoginUI();
            }
        });

        frame.setVisible(true);
    }

    // 切换到好友个人空间面板的方法
    public void showFriendPersonalSpace(User friend) {
        // 如果好友个人空间面板还未初始化，进行初始化
        if (mainPanel.getComponentCount() == 5) { // 这里 5 是因为已有 5 个面板
            PersonalInfoPanel friendPersonalInfoPanel = new PersonalInfoPanel(friend);
            mainPanel.add(friendPersonalInfoPanel, "好友个人空间");
        }

        // 切换到好友个人空间面板
        cardLayout.show(mainPanel, "好友个人空间");
    }
}