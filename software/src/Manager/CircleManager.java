package Manager;

import Entity.Circle;
import Model.CircleInvitation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CircleManager {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // 修改为你的数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 获取所有圈子
    public static List<Circle> getAllCircles() throws SQLException {
        List<Circle> circles = new ArrayList<>();
        String query = "SELECT id, name, creatorId FROM Circles";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int creatorId = resultSet.getInt("creatorId");
                circles.add(new Circle(id, name, creatorId));
            }
        }
        return circles;
    }

    // 创建新圈子
    public static void createCircle(String name, int creatorId) throws SQLException {
        String query = "INSERT INTO Circles (name, creatorId) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setInt(2, creatorId);
            statement.executeUpdate();
        }
    }

    // 解散圈子
    public static void deleteCircle(int circleId) throws SQLException {
        String query = "DELETE FROM Circles WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            statement.executeUpdate();
        }
    }

    // 加入圈子
    public static void joinCircle(int userId, int circleId) throws SQLException {
        String query = "INSERT INTO UserCircles (userId, circleId) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, circleId);
            statement.executeUpdate();
        }
    }

    // 退出圈子
    public static void leaveCircle(int userId, int circleId) throws SQLException {
        String query = "DELETE FROM UserCircles WHERE userId = ? AND circleId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, circleId);
            statement.executeUpdate();
        }
    }

    // 获取圈子的所有信息
    public static List<String> getCircleMessages(int circleId) throws SQLException {
        List<String> messages = new ArrayList<>();
        String query = "SELECT message FROM CircleMessages WHERE circleId = ? ORDER BY postedAt DESC";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(resultSet.getString("message"));
                }
            }
        }
        return messages;
    }

    // 发布信息到圈子
    public static void postMessageToCircle(int circleId, int userId, String message) throws SQLException {
        String query = "INSERT INTO CircleMessages (circleId, userId, message) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            statement.setInt(2, userId);
            statement.setString(3, message);
            statement.executeUpdate();
        }
    }
    //清除信息
    public static void deleteCircleMessages(int circleId) throws SQLException {
        String query = "DELETE FROM CircleMessages WHERE circleId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            statement.executeUpdate();
        }
    }

    //创建邀请
    public static void inviteStudent(int circleId, int studentId) throws SQLException {
        String query = "INSERT INTO CircleInvitations (circleId, studentId) VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            statement.setInt(2, studentId);
            statement.executeUpdate();
        }
    }

    // 获取圈子的待处理邀请
    public static List<CircleInvitation> getPendingInvitations(int circleId) throws SQLException {
        List<CircleInvitation> invitations = new ArrayList<>();
        String query = "SELECT ci.*, u.username AS inviterName, c.name AS circleName " +
                "FROM CircleInvitations ci " +
                "JOIN Circles c ON ci.circleId = c.id " +
                "JOIN Users u ON c.creatorId = u.id " +
                "WHERE ci.circleId = ? AND ci.status = 'pending'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int studentId = resultSet.getInt("studentId");
                    String status = resultSet.getString("status");
                    String inviterName = resultSet.getString("inviterName");
                    String circleName = resultSet.getString("circleName");
                    invitations.add(new CircleInvitation(id, circleId, studentId, status, inviterName, circleName));
                }
            }
        }
        return invitations;
    }

    public static void acceptInvitation(int invitationId) throws SQLException {
        updateInvitationStatus(invitationId, "accepted");
        // 可以在这里添加将学生加入圈子的逻辑
    }
    // 拒绝邀请
    public static void rejectInvitation(int invitationId) throws SQLException {
        updateInvitationStatus(invitationId, "rejected");
    }
    // 更新邀请状态
    private static void updateInvitationStatus(int invitationId, String status) throws SQLException {
        String query = "UPDATE CircleInvitations SET status = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setInt(2, invitationId);
            statement.executeUpdate();
        }
    }
    // 获取学生的待处理邀请

    public static List<CircleInvitation> getPendingInvitationsForStudent(int studentId) throws SQLException {
        List<CircleInvitation> invitations = new ArrayList<>();
        String query = "SELECT ci.*, u.username AS inviterName, c.name AS circleName " +
                "FROM CircleInvitations ci " +
                "JOIN Circles c ON ci.circleId = c.id " +
                "JOIN Users u ON c.creatorId = u.id " +
                "WHERE ci.studentId = ? AND ci.status = 'pending'";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // ... (获取其他属性)
                    String inviterName = resultSet.getString("inviterName");
                    String circleName = resultSet.getString("circleName");
                    int id = resultSet.getInt("id");
                    int circleId = resultSet.getInt("circleId");
                    String status = resultSet.getString("status");


                    CircleInvitation invitation = new CircleInvitation(id, circleId, studentId, status, inviterName, circleName);
                    invitations.add(invitation);
                }
            }
        }
        return invitations;
    }
    // 获取圈子成员列表
    public static List<String> getCircleMembers(int circleId) throws SQLException {
        List<String> members = new ArrayList<>();
        String query = "SELECT u.username " +
                "FROM Users u " +
                "JOIN UserCircles uc ON u.id = uc.userId " +
                "WHERE uc.circleId = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, circleId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    members.add(username);
                }
            }
        }
        return members;
    }

    public static List<String> getCircleMessagesWithUsernames(int circleId) throws SQLException {
        List<String> messagesWithUsernames = new ArrayList<>();
        String query = "SELECT u.username, m.message, m.postedAt " +
                "FROM circlemessages m " +
                "JOIN users u ON m.userId = u.id " +
                "WHERE m.circleId = ? " +
                "ORDER BY m.postedAt ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, circleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String message = rs.getString("message");
                Timestamp postedAt = rs.getTimestamp("postedAt");
                messagesWithUsernames.add(username + " (" + postedAt + "): " + message);
            }
        }
        return messagesWithUsernames;
    }
}