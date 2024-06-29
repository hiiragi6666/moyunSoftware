package UI;

import Manager.FriendService;
import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FriendSearchDialog extends JDialog {
    private JList<String> searchResultList;
    private DefaultListModel<String> searchResultListModel;
    private JButton addFriendButton;
    private FriendService friendService;
    private User user;

    public FriendSearchDialog(Frame owner, User user) {
        super(owner, "搜索结果", true);
        this.user = user;
        this.friendService = new FriendService();

        searchResultListModel = new DefaultListModel<>();
        searchResultList = new JList<>(searchResultListModel);
        JScrollPane searchResultScrollPane = new JScrollPane(searchResultList);

        addFriendButton = new JButton("添加好友");

        addFriendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String friendUsername = searchResultList.getSelectedValue();
                if (friendUsername != null && !friendUsername.isEmpty()) {
                    friendService.sendFriendRequest(user.getUsername(), friendUsername);
                    JOptionPane.showMessageDialog(FriendSearchDialog.this, "好友请求已发送！");
                }
            }
        });

        setLayout(new BorderLayout());
        add(searchResultScrollPane, BorderLayout.CENTER);
        add(addFriendButton, BorderLayout.SOUTH);

        setSize(300, 400);
        setLocationRelativeTo(owner);
    }

    public void updateSearchResults(List<String> results) {
        searchResultListModel.clear();
        for (String result : results) {
            searchResultListModel.addElement(result);
        }
    }
}