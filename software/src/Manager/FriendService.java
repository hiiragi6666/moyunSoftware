package Manager;

import java.sql.SQLException;
import java.util.List;

public class FriendService {
    private FriendDAO friendDAO;

    public FriendService() {
        this.friendDAO = new FriendDAO();
    }

    public List<String> searchUsers(String searchQuery) {
        try {
            return friendDAO.searchUsers(searchQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendFriendRequest(String fromUsername, String toUsername) {
        try {
            friendDAO.sendFriendRequest(fromUsername, toUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getPendingRequests(String username) {
        try {
            return friendDAO.getPendingRequests(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void acceptFriendRequest(String fromUsername, String toUsername) {
        try {
            friendDAO.acceptFriendRequest(fromUsername, toUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rejectFriendRequest(String fromUsername, String toUsername) {
        try {
            friendDAO.rejectFriendRequest(fromUsername, toUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFriend(String username, String friendUsername) {
        try {
            friendDAO.deleteFriend(username, friendUsername);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFriends(String username) {
        try {
            return friendDAO.getFriends(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}