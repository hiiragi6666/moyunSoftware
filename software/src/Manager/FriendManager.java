package Manager;

import Entity.Teacher;
import Entity.User;
import Entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // 修改为你的数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<User> searchUsers(String criteria, String keyword) throws SQLException {
        String query = switch (criteria) {
            case "用户名" -> "SELECT * FROM users WHERE username LIKE ?";
            case "邮箱" -> "SELECT * FROM users WHERE email LIKE ?";
            case "手机号" -> "SELECT * FROM users WHERE phoneNumber LIKE ?";
            default -> "";
        };
        List<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            String searchKeyword = "%" + keyword + "%";
            statement.setString(1, searchKeyword);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("email"),
                            rs.getString("phoneNumber")
                    );
                    users.add(user);
                }
            }
        }
        return users;
    }

    public static void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, email = ?, phoneNumber = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhoneNumber());
            statement.setInt(4, user.getId());

            statement.executeUpdate();
        }
    }

    public static User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phoneNumber");
                    if ("学生".equals(role)) {
                        String studentId = resultSet.getString("studentId");
                        String major = resultSet.getString("major");
                        return new Student(id, username, password, email, phoneNumber, studentId, major);
                    } else if ("教师".equals(role)) {
                        String teacherId = resultSet.getString("teacherId");
                        String department = resultSet.getString("department");
                        return new Teacher(id, username, password, email, phoneNumber, teacherId, department);
                    } else {
                        return new User(id, username, password, role, email, phoneNumber);
                    }
                }
            }
        }
        return null;
    }

    // 获取用户的所有读书心得
    public static List<String> getReadingNotesByUserId(int userId) throws SQLException {
        List<String> notes = new ArrayList<>();
        String query = "SELECT note_content FROM reading_notes WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notes.add(rs.getString("note_content"));
            }
        }
        return notes;
    }
}