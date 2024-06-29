package Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    public List<String> searchUsers(String searchQuery) throws SQLException {
        List<String> results = new ArrayList<>();
        String sql = "SELECT username FROM users WHERE username LIKE ?";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("username"));
            }
        }
        return results;
    }

    public void sendFriendRequest(String fromUsername, String toUsername) throws SQLException {
        int fromUserId = getUserIdByUsername(fromUsername);
        int toUserId = getUserIdByUsername(toUsername);
        String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'pending')";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.executeUpdate();
        }
    }

    public List<String> getPendingRequests(String username) throws SQLException {
        List<String> requests = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        String sql = "SELECT u.username FROM friends f JOIN users u ON f.user_id = u.id WHERE f.friend_id = ? AND f.status = 'pending'";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(rs.getString("username"));
            }
        }
        return requests;
    }

    public void acceptFriendRequest(String fromUsername, String toUsername) throws SQLException {
        int fromUserId = getUserIdByUsername(fromUsername);
        int toUserId = getUserIdByUsername(toUsername);
        String sql = "UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_id = ?";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.executeUpdate();
        }
    }

    public void rejectFriendRequest(String fromUsername, String toUsername) throws SQLException {
        int fromUserId = getUserIdByUsername(fromUsername);
        int toUserId = getUserIdByUsername(toUsername);
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ? AND status = 'pending'";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, fromUserId);
            stmt.setInt(2, toUserId);
            stmt.executeUpdate();
        }
    }

    public void deleteFriend(String username, String friendUsername) throws SQLException {
        int userId = getUserIdByUsername(username);
        int friendId = getUserIdByUsername(friendUsername);
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);
            stmt.executeUpdate();
        }
    }

    public List<String> getFriends(String username) throws SQLException {
        List<String> friends = new ArrayList<>();
        int userId = getUserIdByUsername(username);
        String sql = "SELECT friend_id FROM friends WHERE user_id = ? AND status = 'accepted' " +
                "UNION " +
                "SELECT user_id FROM friends WHERE friend_id = ? AND status = 'accepted'";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friends.add(getUsernameByUserId(rs.getInt(1)));
            }
        }
        return friends;
    }

    private int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("User not found: " + username);
            }
        }
    }

    private String getUsernameByUserId(int userId) throws SQLException {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = CircleManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            } else {
                throw new SQLException("User not found: " + userId);
            }
        }
    }
}