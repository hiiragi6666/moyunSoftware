package UI;

import Entity.Circle;
import Model.CircleInvitation;
import Manager.CircleManager;
import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CirclePanel extends JPanel {
    private User user;
    private DefaultListModel<Circle> circleListModel;
    private JList<Circle> circleList;
    private JTextArea membersArea;
    private JTextArea messagesArea;
    private JTextField messageInput;
    private JList<CircleInvitation> invitationList;
    private DefaultListModel<CircleInvitation> invitationListModel;

    public CirclePanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        // 圈子列表
        circleListModel = new DefaultListModel<>();
        circleList = new JList<>(circleListModel);
        circleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane circleListScrollPane = new JScrollPane(circleList);
        add(circleListScrollPane, BorderLayout.WEST);

        // 成员信息显示区域
        membersArea = new JTextArea("成员信息：\n", 5, 30);
        membersArea.setEditable(false);
        JScrollPane membersScrollPane = new JScrollPane(membersArea);

        // 消息显示区域
        messagesArea = new JTextArea("消息：\n", 5, 30);
        messagesArea.setEditable(false);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);

        // 将两个显示区域添加到面板
        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.add(membersScrollPane);
        displayPanel.add(messagesScrollPane);
        add(displayPanel, BorderLayout.CENTER);

        // 输入框和发送按钮
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageInput = new JTextField();
        JButton sendButton = new JButton("发送");
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // 邀请列表
        invitationListModel = new DefaultListModel<>();
        invitationList = new JList<>(invitationListModel);
        JScrollPane invitationScrollPane = new JScrollPane(invitationList);
        invitationScrollPane.setBorder(BorderFactory.createTitledBorder("邀请信息："));
        if (!"教师".equals(user.getRole())) {
            invitationScrollPane.setVisible(true);
            add(invitationScrollPane, BorderLayout.EAST);
            loadPendingInvitations();
        }

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        JButton createCircleButton = new JButton("创建圈子");
        JButton joinCircleButton = new JButton("加入圈子");
        JButton leaveCircleButton = new JButton("退出圈子");
        JButton disbandCircleButton = new JButton("解散圈子");
        JButton inviteStudentButton = new JButton("邀请学生");
        buttonPanel.add(createCircleButton);
        buttonPanel.add(joinCircleButton);
        buttonPanel.add(leaveCircleButton);
        buttonPanel.add(disbandCircleButton);
        buttonPanel.add(inviteStudentButton);
        add(buttonPanel, BorderLayout.NORTH);

        if ("教师".equals(user.getRole())) {
            createCircleButton.setVisible(true);
            disbandCircleButton.setVisible(true);
            inviteStudentButton.setVisible(true);
        } else {
            createCircleButton.setVisible(false);
            disbandCircleButton.setVisible(false);
            inviteStudentButton.setVisible(false);
        }

        // 事件监听器
        circleList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Circle selectedCircle = circleList.getSelectedValue();
                if (selectedCircle != null) {
                    displayMessages(selectedCircle.getId());
                } else {
                    messagesArea.setText("");
                }
            }
        });

        createCircleButton.addActionListener(e -> createCircle());
        joinCircleButton.addActionListener(e -> joinCircle());
        leaveCircleButton.addActionListener(e -> leaveCircle());
        disbandCircleButton.addActionListener(e -> disbandCircle());
        inviteStudentButton.addActionListener(e -> inviteStudent());
        sendButton.addActionListener(e -> sendMessage());

        loadCircles();
    }

    private void loadCircles() {
        try {
            circleListModel.clear();
            List<Circle> circles = CircleManager.getAllCircles();
            for (Circle circle : circles) {
                circleListModel.addElement(circle);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载圈子失败: " + e.getMessage());
        }
    }

    private void createCircle() {
        if ("教师".equals(user.getRole())) {
            String circleName = JOptionPane.showInputDialog("请输入圈子名称:");
            if (circleName != null && !circleName.isEmpty()) {
                try {
                    CircleManager.createCircle(circleName, user.getId());
                    loadCircles();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "创建圈子失败: " + e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "只有教师可以创建圈子！");
        }
    }

    private void joinCircle() {
        Circle selectedCircle = circleList.getSelectedValue();
        if (selectedCircle != null) {
            try {
                CircleManager.joinCircle(user.getId(), selectedCircle.getId());
                JOptionPane.showMessageDialog(this, "成功加入圈子 " + selectedCircle.getName());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "加入圈子失败: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要加入的圈子！");
        }
    }

    private void leaveCircle() {
        Circle selectedCircle = circleList.getSelectedValue();
        if (selectedCircle != null) {
            try {
                CircleManager.leaveCircle(user.getId(), selectedCircle.getId());
                JOptionPane.showMessageDialog(this, "成功退出圈子 " + selectedCircle.getName());
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "退出圈子失败: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要退出的圈子！");
        }
    }

    private void disbandCircle() {
        Circle selectedCircle = circleList.getSelectedValue();
        if (selectedCircle != null) {
            if ("教师".equals(user.getRole()) && user.getId() == selectedCircle.getCreatorId()) {
                try {
                    CircleManager.deleteCircle(selectedCircle.getId());
                    loadCircles();
                    JOptionPane.showMessageDialog(this, "成功解散圈子 " + selectedCircle.getName());
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "解散圈子失败: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "只有圈子的创建者才能解散圈子！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要解散的圈子！");
        }
    }

    private void sendMessage() {
        Circle selectedCircle = circleList.getSelectedValue();
        if (selectedCircle != null) {
            String message = messageInput.getText().trim();
            if (!message.isEmpty()) {
                try {
                    CircleManager.postMessageToCircle(selectedCircle.getId(), user.getId(), message);
                    messageInput.setText("");
                    displayMessages(selectedCircle.getId());
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "发送消息失败: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "消息内容不能为空！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要发送消息的圈子！");
        }
    }

    private void displayMessages(int circleId) {
        try {
            List<String> members = CircleManager.getCircleMembers(circleId);
            membersArea.setText("成员信息：\n");
            for (String member : members) {
                membersArea.append(member + "\n");
            }

            List<String> messages = CircleManager.getCircleMessagesWithUsernames(circleId);
            messagesArea.setText("消息：\n");
            for (String message : messages) {
                messagesArea.append(message + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载圈子信息失败: " + e.getMessage());
        }
    }

    private void inviteStudent() {
        if ("教师".equals(user.getRole())) {
            Circle selectedCircle = circleList.getSelectedValue();
            if (selectedCircle != null) {
                String studentIdString = JOptionPane.showInputDialog(this, "请输入要邀请的学生ID：");
                if (studentIdString != null && !studentIdString.isEmpty()) {
                    try {
                        int studentId = Integer.parseInt(studentIdString);
                        CircleManager.inviteStudent(selectedCircle.getId(), studentId);
                        JOptionPane.showMessageDialog(this, "邀请已发送！");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "无效的学生ID！");
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "发送邀请失败: " + e.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "请选择要邀请学生的圈子！");
            }
        } else {
            JOptionPane.showMessageDialog(this, "只有教师可以邀请学生！");
        }
    }

    private void loadPendingInvitations() {
        try {
            List<CircleInvitation> invitations = CircleManager.getPendingInvitations(user.getId());
            invitationListModel.clear();
            for (CircleInvitation invitation : invitations) {
                invitationListModel.addElement(invitation);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "加载邀请信息失败: " + e.getMessage());
        }
    }
}