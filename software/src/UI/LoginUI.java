package UI;

import Entity.Student;
import Entity.Teacher;
import Entity.User;
import Manager.UserManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public LoginUI() {
        frame = new JFrame("登录");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 250);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setBounds(10, 80, 80, 25);
        panel.add(roleLabel);

        roleComboBox = new JComboBox<>(new String[]{"学生", "老师", "管理员"});
        roleComboBox.setBounds(100, 80, 165, 25);
        panel.add(roleComboBox);

        JButton loginButton = new JButton("登录");
        loginButton.setBounds(10, 120, 80, 25);
        panel.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    User user = UserManager.getUser(usernameField.getText(), new String(passwordField.getPassword()));
                    if (user != null) {
                        JOptionPane.showMessageDialog(frame, "登录成功");
                        frame.dispose(); // 关闭登录界面
                        if ("管理员".equals(user.getRole())) {
                            new AdminUI(user); // 管理员登录后进入管理员界面
                        } else {
                            new MainUI(user); // 普通用户登录后进入普通用户界面
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "用户名或密码错误");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton forgotPasswordButton = new JButton("忘记密码");
        forgotPasswordButton.setBounds(100, 120, 120, 25);
        panel.add(forgotPasswordButton);
        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showForgotPasswordDialog();
            }
        });

        JButton registerButton = new JButton("注册");
        registerButton.setBounds(230, 120, 80, 25);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterDialog();
            }
        });
    }

    private void showForgotPasswordDialog() {
        JFrame forgotPasswordFrame = new JFrame("忘记密码");
        forgotPasswordFrame.setSize(350, 350);

        JPanel panel = new JPanel();
        forgotPasswordFrame.add(panel);
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 200, 25);
        panel.add(usernameField);

        JButton generateResetCodeButton = new JButton("生成重置代码");
        generateResetCodeButton.setBounds(100, 50, 150, 25);
        panel.add(generateResetCodeButton);
        generateResetCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                try {
                    String resetCode = UserManager.generateResetCode(username);
                    if (resetCode != null) {
                        JOptionPane.showMessageDialog(forgotPasswordFrame, "重置代码已生成，请记下: " + resetCode);
                    } else {
                        JOptionPane.showMessageDialog(forgotPasswordFrame, "用户名不存在");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "生成重置代码失败，请稍后重试");
                }
            }
        });

        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setBounds(10, 80, 80, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField(20);
        emailField.setBounds(100, 80, 200, 25);
        panel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("手机号:");
        phoneNumberLabel.setBounds(10, 110, 80, 25);
        panel.add(phoneNumberLabel);

        JTextField phoneNumberField = new JTextField(20);
        phoneNumberField.setBounds(100, 110, 200, 25);
        panel.add(phoneNumberField);

        JLabel resetCodeLabel = new JLabel("重置代码:");
        resetCodeLabel.setBounds(10, 140, 80, 25);
        panel.add(resetCodeLabel);

        JTextField resetCodeField = new JTextField(20);
        resetCodeField.setBounds(100, 140, 200, 25);
        panel.add(resetCodeField);

        JLabel newPasswordLabel = new JLabel("新密码:");
        newPasswordLabel.setBounds(10, 170, 80, 25);
        panel.add(newPasswordLabel);

        JPasswordField newPasswordField = new JPasswordField(20);
        newPasswordField.setBounds(100, 170, 200, 25);
        panel.add(newPasswordField);

        JButton setNewPasswordButton = new JButton("设置新密码");
        setNewPasswordButton.setBounds(100, 200, 150, 25);
        panel.add(setNewPasswordButton);
        setNewPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String phoneNumber = phoneNumberField.getText();
                String resetCode = resetCodeField.getText();
                String newPassword = new String(newPasswordField.getPassword());
                try {
                    boolean success = UserManager.resetPassword(username, resetCode, newPassword, email, phoneNumber);
                    if (success) {
                        JOptionPane.showMessageDialog(forgotPasswordFrame, "密码重置成功");
                        forgotPasswordFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(forgotPasswordFrame, "重置失败，请检查信息是否正确");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(forgotPasswordFrame, "重置失败，请稍后重试");
                }
            }
        });

        forgotPasswordFrame.setVisible(true);
    }

    private void showRegisterDialog() {
        JFrame registerFrame = new JFrame("注册");
        registerFrame.setSize(350, 300);

        JPanel panel = new JPanel();
        registerFrame.add(panel);
        panel.setLayout(null);

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 165, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JLabel emailLabel = new JLabel("邮箱:");
        emailLabel.setBounds(10, 80, 80, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField(20);
        emailField.setBounds(100, 80, 165, 25);
        panel.add(emailField);

        JLabel phoneNumberLabel = new JLabel("手机号:");
        phoneNumberLabel.setBounds(10, 110, 80, 25);
        panel.add(phoneNumberLabel);

        JTextField phoneNumberField = new JTextField(20);
        phoneNumberField.setBounds(100, 110, 165, 25);
        panel.add(phoneNumberField);

        JButton registerButton = new JButton("注册");
        registerButton.setBounds(100, 150, 120, 25);
        panel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRole = (String) roleComboBox.getSelectedItem();
                try {
                    String email = emailField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    if ("学生".equals(selectedRole)) {
                        UserManager.createUser(new Student(0, usernameField.getText(), new String(passwordField.getPassword()), email, phoneNumber, "studentId", "major"));
                    } else if ("老师".equals(selectedRole)) {
                        UserManager.createUser(new Teacher(0, usernameField.getText(), new String(passwordField.getPassword()), email, phoneNumber, "teacherId", "department"));
                    }
                    JOptionPane.showMessageDialog(registerFrame, "注册成功");
                    registerFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(registerFrame, "注册失败，请检查输入或稍后重试");
                }
            }
        });

        registerFrame.setVisible(true);
    }
}
