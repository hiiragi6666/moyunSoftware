package UI;


import Manager.FriendManager;
import Entity.User;
import Manager.UserManager;
import Model.UserTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private UserTableModel userTableModel;
    private JButton deleteUserButton;
    private JComboBox<String> searchCriteriaComboBox;
    private JTextField searchField;
    private JButton searchButton;

    public UserManagementPanel() {
        // 添加用户管理的组件和逻辑
        setLayout(new BorderLayout());

        // 创建顶部面板，包含搜索条件下拉框、搜索框和按钮
        JPanel topPanel = new JPanel(new FlowLayout());
        searchCriteriaComboBox = new JComboBox<>(new String[]{"用户名", "邮箱", "手机号"});
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");

        topPanel.add(searchCriteriaComboBox);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // 获取用户数据并创建表模型
        userTableModel = new UserTableModel();
        userTable = new JTable(userTableModel);

        // 创建删除用户按钮
        deleteUserButton = new JButton("删除用户");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该用户吗？", "删除用户", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int userId = (int) userTableModel.getValueAt(selectedRow, 0);
                        try {
                            UserManager.deleteUser(userId);
                            userTableModel.refreshData();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "删除用户失败");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请选择一个用户进行删除");
                }
            }
        });

        // 添加搜索按钮的事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                String criteria = (String) searchCriteriaComboBox.getSelectedItem();
                if (keyword.isEmpty()) {
                    refreshUserList();
                } else {
                    try {
                        List<User> user = FriendManager.searchUsers(criteria, keyword);
                        updateTable(user);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(UserManagementPanel.this, "搜索失败，请稍后重试");
                    }
                }
            }
        });

        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(deleteUserButton, BorderLayout.SOUTH);

        // 初始化用户数据
        refreshUserList();

    }


    private void refreshUserList() {
        try {
            List<User> users = UserManager.getAllUsers();
            updateTable(users);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "获取用户列表失败");
        }
    }

    private void updateTable(List<User> users) {
        userTableModel.setUsers(users);
    }
}