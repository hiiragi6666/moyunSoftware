package UI;

import Entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookDialog extends JDialog {
    private JTextField idField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField yearField;
    private JTextField isbnField;
    private JTextField categoryField;
    private JTextArea descriptionArea;
    private boolean confirmed;

    public BookDialog(Book book) {
        setTitle("书籍信息");
        setModal(true);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 2));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEnabled(false); // ID 字段不可编辑

        // 自动生成ID，可以根据需要修改生成规则
        if (book == null) {
            idField.setText(generateNextId()); // 生成下一个ID
        } else {
            idField.setText(String.valueOf(book.getId()));
        }
        formPanel.add(idField);

        formPanel.add(new JLabel("标题:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("作者:"));
        authorField = new JTextField();
        formPanel.add(authorField);

        formPanel.add(new JLabel("出版社:"));
        publisherField = new JTextField();
        formPanel.add(publisherField);

        formPanel.add(new JLabel("年份:"));
        yearField = new JTextField();
        formPanel.add(yearField);

        formPanel.add(new JLabel("ISBN:"));
        isbnField = new JTextField();
        formPanel.add(isbnField);

        formPanel.add(new JLabel("类别:"));
        categoryField = new JTextField();
        formPanel.add(categoryField);

        formPanel.add(new JLabel("描述:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });

        pack();
    }

    private String generateNextId() {
        // 在实际应用中，可能需要从数据库或其他地方获取下一个可用的ID
        // 这里简单地生成一个随机数作为示例
        return String.valueOf((int) (Math.random() * 1000));
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Book getBook() {
        return new Book(
                Integer.parseInt(idField.getText()),
                titleField.getText(),
                authorField.getText(),
                publisherField.getText(),
                descriptionArea.getText(),
                Integer.parseInt(yearField.getText()),
                isbnField.getText(),
                categoryField.getText()


        );
    }
}