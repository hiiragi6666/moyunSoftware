package UI;

import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI {
    private JFrame frame;
    private JPanel mainPanel;
    private BookLibraryPanel bookLibraryPanel;
    private UserManagementPanel userManagementPanel;
    private BookManagementPanel bookManagementPanel;
    private CirclePanel circleMainPanel;
    private CardLayout cardLayout;
    private User user;

    public AdminUI(User user) {
        this.user = user;

        frame = new JFrame("管理员界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        bookLibraryPanel = new BookLibraryPanel(user);
        mainPanel.add(bookLibraryPanel, "书库");

        userManagementPanel = new UserManagementPanel();
        mainPanel.add(userManagementPanel, "用户管理");

        bookManagementPanel = new BookManagementPanel();
        mainPanel.add(bookManagementPanel, "书籍管理");

        circleMainPanel = new CirclePanel(user);
        mainPanel.add(circleMainPanel, "圈子管理");

        JPanel buttonPanel = new JPanel();
        JButton bookLibraryButton = new JButton("书库");
        JButton userManagementButton = new JButton("用户管理");
        JButton bookManagementButton = new JButton("书籍管理");
        JButton circleManagementButton = new JButton("圈子管理");
        JButton logoutButton = new JButton("注销");

        buttonPanel.add(bookLibraryButton);
        buttonPanel.add(userManagementButton);
        buttonPanel.add(bookManagementButton);
        buttonPanel.add(circleManagementButton);
        buttonPanel.add(logoutButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        circleManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "圈子管理");
            }
        });

        bookLibraryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "书库");
            }
        });

        userManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "用户管理");
            }
        });

        bookManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "书籍管理");
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

}