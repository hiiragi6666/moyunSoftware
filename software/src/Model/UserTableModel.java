package Model;

import Entity.User;
import Manager.UserManager;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserTableModel extends AbstractTableModel {
    private List<User> users;
    private String[] columnNames = {"ID", "username", "role", "email", "phoneNumber"};

    public UserTableModel() {
        refreshData();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        switch (columnIndex) {
            case 0: return user.getId();
            case 1: return user.getUsername();
            case 2: return user.getRole();
            case 3: return user.getEmail();
            case 4: return user.getPhoneNumber();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void refreshData() {
        try {
            List<User> allUsers = UserManager.getAllUsers();
            // 过滤掉管理员账号
            users = allUsers.stream()
                    .filter(user -> !"admin".equals(user.getRole()))
                    .collect(Collectors.toList());
            //users = DatabaseManager.getAllUsers();
            fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}