package UI;

import Manager.FriendManager;
import Manager.FriendService;
import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class FriendManagementPanel extends JPanel {
    private User user;
    private JList<String> friendList;
    private DefaultListModel<String> friendListModel;
    private JList<String> pendingList;
    private DefaultListModel<String> pendingListModel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton acceptFriendButton;
    private JButton rejectFriendButton;
    private JButton deleteFriendButton;
    private FriendService friendService;
    private FriendSearchDialog friendSearchDialog;

    public FriendManagementPanel(User user) {
        this.user = user;
        this.friendService = new FriendService();

        setLayout(new BorderLayout());

        friendListModel = new DefaultListModel<>();
        friendList = new JList<>(friendListModel);
        JScrollPane friendScrollPane = new JScrollPane(friendList);

        pendingListModel = new DefaultListModel<>();
        pendingList = new JList<>(pendingListModel);
        JScrollPane pendingScrollPane = new JScrollPane(pendingList);

        // 搜索面板
        JPanel searchPanel = new JPanel();
        searchTextField = new JTextField(15);
        searchButton = new JButton("搜索");

        searchPanel.add(new JLabel("搜索用户:"));
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);

        // 控制面板 - 左侧（好友列表）
        JPanel friendPanel = new JPanel(new BorderLayout());
        friendPanel.add(new JLabel("好友列表"), BorderLayout.NORTH);
        friendPanel.add(friendScrollPane, BorderLayout.CENTER);

        // 控制面板 - 右侧（待处理请求）
        JPanel pendingPanel = new JPanel(new BorderLayout());
        pendingPanel.add(new JLabel("待处理请求"), BorderLayout.NORTH);
        pendingPanel.add(pendingScrollPane, BorderLayout.CENTER);

        // 接受请求按钮
        acceptFriendButton = new JButton("同意请求");
        // 拒绝请求按钮
        rejectFriendButton = new JButton("拒绝请求");

        JPanel acceptRejectPanel = new JPanel();
        acceptRejectPanel.add(acceptFriendButton);
        acceptRejectPanel.add(rejectFriendButton);
        pendingPanel.add(acceptRejectPanel, BorderLayout.EAST);

        // 删除好友按钮
        deleteFriendButton = new JButton("删除好友");
        JPanel actionPanel = new JPanel();
        actionPanel.add(deleteFriendButton);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, friendPanel, pendingPanel);
        splitPane.setDividerLocation(500); // 设置分割线位置

        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        friendSearchDialog = new FriendSearchDialog((Frame) SwingUtilities.getWindowAncestor(this), user);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchQuery = searchTextField.getText();
                if (!searchQuery.isEmpty()) {
                    List<String> results = friendService.searchUsers(searchQuery);
                    friendSearchDialog.updateSearchResults(results);
                    friendSearchDialog.setVisible(true);
                }
            }
        });

        acceptFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pendingRequest = pendingList.getSelectedValue();
                if (pendingRequest != null && !pendingRequest.isEmpty()) {
                    friendService.acceptFriendRequest(pendingRequest, user.getUsername());
                    pendingListModel.removeElement(pendingRequest);
                    loadFriends();
                    JOptionPane.showMessageDialog(null, "好友请求已接受！");
                }
            }
        });

        rejectFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pendingRequest = pendingList.getSelectedValue();
                if (pendingRequest != null && !pendingRequest.isEmpty()) {
                    friendService.rejectFriendRequest(pendingRequest, user.getUsername());
                    pendingListModel.removeElement(pendingRequest);
                    JOptionPane.showMessageDialog(null, "好友请求已拒绝！");
                }
            }
        });

        deleteFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFriend = friendList.getSelectedValue();
                if (selectedFriend != null) {
                    friendService.deleteFriend(user.getUsername(), selectedFriend);
                    friendListModel.removeElement(selectedFriend);
                    JOptionPane.showMessageDialog(null, "好友已删除！");
                }
            }
        });

        friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedFriend = friendList.getSelectedValue();
                    if (selectedFriend != null) {
                        try {
                            User friendUser = FriendManager.getUserByUsername(selectedFriend);
                            if (friendUser != null) {
                                FriendProfileDialog dialog = new FriendProfileDialog((Frame) SwingUtilities.getWindowAncestor(FriendManagementPanel.this), friendUser);
                                dialog.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "无法获取好友信息：" + selectedFriend);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "数据库操作失败：" + ex.getMessage());
                        }
                    }
                }
            }
        });

        loadFriends();
        loadPendingRequests();
    }

    private void loadFriends() {
        List<String> friends = friendService.getFriends(user.getUsername());
        friendListModel.clear();
        for (String friend : friends) {
            friendListModel.addElement(friend);
        }
    }

    private void loadPendingRequests() {
        List<String> pendingRequests = friendService.getPendingRequests(user.getUsername());
        pendingListModel.clear();
        for (String request : pendingRequests) {
            pendingListModel.addElement(request);
        }
    }
}