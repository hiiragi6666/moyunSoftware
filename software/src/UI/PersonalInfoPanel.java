package UI;

import Manager.FriendManager;
import Entity.Student;
import Entity.User;
import Manager.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonalInfoPanel extends JPanel {
    private JLabel usernameLabel;
    private JLabel roleLabel;
    private JTextArea infoTextArea; // 使用 JTextArea 显示用户信息和个人空间内容
    private User user;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel userInfoPanel;
    private JPanel personalSpacePanel;
    private JPanel notesPanel; // 显示读书心得的面板
    private List<String> readingNotes; // 存储读书心得的列表
    private JList<String> notesList; // 用于显示心得的 JList
    private JLabel personalSpaceLabel; // 新增的标签，用于显示个人空间欢迎信息

    public PersonalInfoPanel(User user) {
        this.user = user;
        this.readingNotes = new ArrayList<>(); //初始化读书心得列表
        loadReadingNotesFromDatabase(); // 从数据库加载心得
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setLayout(new BorderLayout());

        // 创建信息显示区域
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false); // 设置为不可编辑
        updateInfo(); // 更新显示的用户信息
        JScrollPane scrollPane = new JScrollPane(infoTextArea);

        // 创建进入个人空间按钮
        JButton personalSpaceButton = new JButton("进入个人空间");
        personalSpaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPersonalSpace();
            }
        });

        // 创建修改信息按钮
        JButton editInfoButton = new JButton("修改信息");
        editInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editUserInfo();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(personalSpaceButton);
        buttonPanel.add(editInfoButton);



        // 初始化个人空间面板
        personalSpacePanel = new JPanel(new BorderLayout());

        // 新增个人空间标签
        personalSpaceLabel = new JLabel();
        personalSpacePanel.add(personalSpaceLabel, BorderLayout.NORTH);

        notesPanel = new JPanel();
        notesPanel.setLayout(new BoxLayout(notesPanel, BoxLayout.Y_AXIS));


        // 初始化 JList 用于显示心得
        notesList = new JList<>();
        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);



        // 创建显示用户信息的面板
        userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.add(scrollPane, BorderLayout.CENTER);
        userInfoPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 创建显示个人空间的面板
        personalSpacePanel.add(new JScrollPane(notesPanel), BorderLayout.CENTER);
        personalSpacePanel.add(new JScrollPane(notesList), BorderLayout.CENTER);
        JButton returnButton = new JButton("返回个人信息");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showUserInfo();
            }
        });

        JPanel personalSpaceButtonPanel = new JPanel();
        personalSpaceButtonPanel.add(returnButton);

        JButton addNoteButton = new JButton("发表心得");
        JButton deleteNoteButton = new JButton("删除心得");
        JButton editNoteButton = new JButton("修改心得");
        addNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNote();
            }
        });

        deleteNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNote();
            }
        });

        editNoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editNote();
            }
        });
        personalSpaceButtonPanel.add(addNoteButton);
        personalSpaceButtonPanel.add(deleteNoteButton);
        personalSpaceButtonPanel.add(editNoteButton);

        //personalSpacePanel.add(new JScrollPane(personalSpaceText), BorderLayout.CENTER);
        personalSpacePanel.add(personalSpaceButtonPanel, BorderLayout.SOUTH);

        // 将信息区域和按钮区域添加到主面板
        add(mainPanel, BorderLayout.CENTER);

        // 将用户信息面板和个人空间面板添加到主面板中管理
        mainPanel.add(userInfoPanel, "UserInfo");
        mainPanel.add(personalSpacePanel, "PersonalSpace");

        // 默认显示用户信息面板
        cardLayout.show(mainPanel, "UserInfo");
    }

    private void loadReadingNotesFromDatabase() {
        try {
            List<String> notesFromDb = UserManager.getReadingNotesByUserId(user.getId());
            readingNotes.addAll(notesFromDb);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新信息显示区域的内容
    private void updateInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("用户名: ").append(user.getUsername()).append("\n");
        sb.append("角色: ").append(user.getRole()).append("\n");
        sb.append("邮箱: ").append(user.getEmail()).append("\n");
        sb.append("手机号: ").append(user.getPhoneNumber()).append("\n");

        if (user instanceof Student) {
            sb.append("学生ID: ").append(((Student) user).getStudentId()).append("\n");
        }

        infoTextArea.setText(sb.toString());
    }

    // 切换到个人空间面板
    private void showPersonalSpace() {
        updatePersonalSpace(); // 每次显示个人空间时更新内容
        cardLayout.show(mainPanel, "PersonalSpace");
    }

    // 切换到用户信息面板
    private void showUserInfo() {
        updateInfo();
        cardLayout.show(mainPanel, "UserInfo");
    }

    // 显示编辑信息对话框
    private void editUserInfo() throws SQLException {
        JTextField usernameField = new JTextField(user.getUsername());
        JTextField emailField = new JTextField(user.getEmail());
        JTextField phoneNumberField = new JTextField(user.getPhoneNumber());

        JPanel editPanel = new JPanel(new GridLayout(3, 2));
        editPanel.add(new JLabel("用户名:"));
        editPanel.add(usernameField);
        editPanel.add(new JLabel("邮箱:"));
        editPanel.add(emailField);
        editPanel.add(new JLabel("手机号:"));
        editPanel.add(phoneNumberField);

        int result = JOptionPane.showConfirmDialog(this, editPanel, "修改信息", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            user.setUsername(usernameField.getText());
            user.setEmail(emailField.getText());
            user.setPhoneNumber(phoneNumberField.getText());
            saveChanges();
            updateInfo();
        }
    }

    // 保存更改到数据库
    private void saveChanges() throws SQLException {
        FriendManager.updateUser(user);
    }


    // 更新个人空间显示区域的内容
    private void updatePersonalSpace() {
        personalSpaceLabel.setText("这是 " + user.getUsername() + " 的个人空间");
        notesPanel.removeAll();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < readingNotes.size(); i++) {
            String note = readingNotes.get(i);
            listModel.addElement("心得 " + (i + 1) + ": " + note);
            JLabel noteLabel = new JLabel(note);
            noteLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            notesPanel.add(noteLabel);
        }
        notesList.setModel(listModel);
        notesPanel.revalidate();
        notesPanel.repaint();
    }

    private void addNote() {
        String note = JOptionPane.showInputDialog(this, "请输入读书心得:");
        if (note != null && !note.trim().isEmpty()) {
            readingNotes.add(note);
            try {
                UserManager.addReadingNoteToDatabase(user.getId(), note);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updatePersonalSpace();
        }
    }

    private void deleteNote() {
        int selectedIndex = notesList.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "确定删除心得 " + (selectedIndex + 1) + " 吗?", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String noteToDelete = readingNotes.remove(selectedIndex);
                try {
                    UserManager.deleteReadingNoteFromDatabase(user.getId(), noteToDelete);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updatePersonalSpace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的心得");
        }
    }

    private void editNote() {
        int selectedIndex = notesList.getSelectedIndex();
        if (selectedIndex != -1) {
            String newNote = JOptionPane.showInputDialog(this, "修改心得:", readingNotes.get(selectedIndex));
            if (newNote != null && !newNote.trim().isEmpty()) {
                String oldNote = readingNotes.set(selectedIndex, newNote);
                try {
                    UserManager.updateReadingNoteInDatabase(user.getId(), oldNote, newNote);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updatePersonalSpace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要修改的心得");
        }
    }

}