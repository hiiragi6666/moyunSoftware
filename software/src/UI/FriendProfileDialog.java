package UI;

import Manager.FriendManager;
import Entity.Student;
import Entity.Teacher;
import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class FriendProfileDialog extends JDialog {
    private User friend;

    public FriendProfileDialog(Frame owner, User friend) {
        super(owner, "好友个人信息", true);
        this.friend = friend;

        JPanel panel = new JPanel(new BorderLayout());

        // 详细信息
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false); // 设置为不可编辑
        infoTextArea.append("用户名: " + friend.getUsername() + "\n");
        infoTextArea.append("角色: " + friend.getRole() + "\n");
        infoTextArea.append("邮箱: " + friend.getEmail() + "\n");
        infoTextArea.append("手机号: " + friend.getPhoneNumber() + "\n");

        // 如果是学生或老师，显示特定信息
        if (friend instanceof Student) {
            infoTextArea.append("学生ID: " + ((Student) friend).getStudentId() + "\n");
        } else if (friend instanceof Teacher) {
            infoTextArea.append("老师ID: " + ((Teacher) friend).getTeacherId() + "\n");
        }

        panel.add(new JScrollPane(infoTextArea), BorderLayout.CENTER);

        // 进入个人空间按钮
        JButton enterSpaceButton = new JButton("进入个人空间");
        enterSpaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFriendPersonalSpace();
                dispose();
            }
        });

        panel.add(enterSpaceButton, BorderLayout.SOUTH);

        add(panel);
        setSize(300, 300);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // 显示好友个人空间的方法
    private void showFriendPersonalSpace() {
        JDialog personalSpaceDialog = new JDialog(this, "好友的个人空间", true);
        personalSpaceDialog.setSize(400, 400);
        personalSpaceDialog.setLocationRelativeTo(this);

        JTextArea personalSpaceArea = new JTextArea();
        personalSpaceArea.setEditable(false);
        personalSpaceArea.append("欢迎来到 " + friend.getUsername() + " 的个人空间!\n");

        // 从数据库获取好友的读书心得
        try {
            List<String> friendNotes = FriendManager.getReadingNotesByUserId(friend.getId());
            if (!friendNotes.isEmpty()) {
                personalSpaceArea.append("好友的读书心得:\n");
                for (int i = 0; i < friendNotes.size(); i++) {
                    personalSpaceArea.append("心得" + (i + 1) + ". " + friendNotes.get(i) + "\n");
                }
            } else {
                personalSpaceArea.append("好友尚未发布任何读书心得。\n");
            }
        } catch (SQLException e) {
            personalSpaceArea.append("加载好友的读书心得时出错。\n");
            e.printStackTrace();
        }

        personalSpaceDialog.add(new JScrollPane(personalSpaceArea));
        personalSpaceDialog.setVisible(true);
    }
}