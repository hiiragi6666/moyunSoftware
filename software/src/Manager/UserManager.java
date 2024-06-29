package Manager;

import Entity.Teacher;
import Entity.User;
import Entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserManager {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // 修改为你的数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static User getUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String role = resultSet.getString("role");
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



    public static void createUser(User user) throws SQLException {
        String userQuery = "INSERT INTO Users (username, password, role, email, phoneNumber, studentId, teacherId, major, department) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(userQuery)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());

            if (user instanceof Student) {
                String studentId = generateStudentId(); // 生成随机学生ID
                stmt.setString(6, studentId);
                stmt.setString(7, ((Student) user).getMajor());
                stmt.setString(8, null);
                stmt.setString(9, null);
            } else if (user instanceof Teacher) {
                String teacherId = generateTeacherId(); // 生成随机老师ID
                stmt.setString(6, null);
                stmt.setString(7, teacherId);
                stmt.setString(8, null);
                stmt.setString(9, ((Teacher) user).getDepartment());
            }

            stmt.executeUpdate();
        }
    }

    // 获取所有用户
    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");

        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String role = rs.getString("role");
            String email = rs.getString("email");
            String phoneNumber = rs.getString("phoneNumber");

            users.add(new User(id, username, password, role, email, phoneNumber));
        }

        rs.close();
        stmt.close();
        conn.close();
        return users;
    }

    // 删除用户
    public static void deleteUser(int userId) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        pstmt.setInt(1, userId);
        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
    }

    private static String generateStudentId() {
        // 生成随机学生ID的逻辑，可以根据需要自行编写
        Random random = new Random();
        int randomId = 100000 + random.nextInt(900000); // 生成一个6位数的随机学生ID
        return "S" + randomId; // 假设学生ID以S开头
    }

    private static String generateTeacherId() {
        // 生成随机学生ID的逻辑，可以根据需要自行编写
        Random random = new Random();
        int randomId = 100000 + random.nextInt(900000); // 生成一个6位数的随机学生ID
        return "T" + randomId; // 假设学生ID以S开头
    }



    public static String generateResetCode(String username) throws SQLException {
        String resetCode = generateRandomCode(6); // 生成6位随机码
        String query = "UPDATE Users SET reset_code = ? WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, resetCode);
            statement.setString(2, username);
            int rows = statement.executeUpdate();
            if (rows > 0) {
                return resetCode;
            } else {
                return null;
            }
        }
    }

    private static String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    // 添加心得到数据库
    public static void addReadingNoteToDatabase(int userId, String note) throws SQLException {
        String query = "INSERT INTO reading_notes (user_id, note_content) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, note);
            stmt.executeUpdate();
        }
    }

    // 从数据库删除心得
    public static void deleteReadingNoteFromDatabase(int userId, String note) throws SQLException {
        String query = "DELETE FROM reading_notes WHERE user_id = ? AND note_content = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, note);
            stmt.executeUpdate();
        }
    }

    // 更新数据库中的心得
    public static void updateReadingNoteInDatabase(int userId, String oldNote, String newNote) throws SQLException {
        String query = "UPDATE reading_notes SET note_content = ? WHERE user_id = ? AND note_content = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newNote);
            stmt.setInt(2, userId);
            stmt.setString(3, oldNote);
            stmt.executeUpdate();
        }
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


    public static boolean resetPassword(String username, String resetCode, String newPassword, String email, String phoneNumber) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND reset_code = ? AND email = ? AND phoneNumber = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, resetCode);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    // 验证通过，更新密码
                    String updateSql = "UPDATE users SET password = ? WHERE username = ?";
                    try (PreparedStatement updatePstmt = connection.prepareStatement(updateSql)) {
                        updatePstmt.setString(1, newPassword);
                        updatePstmt.setString(2, username);
                        updatePstmt.executeUpdate();
                        return true;
                    }
                } else {
                    return false; // 验证失败
                }
            }
        }
    }
}