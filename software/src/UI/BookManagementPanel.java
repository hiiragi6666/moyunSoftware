package UI;

import Entity.Book;
import Manager.BookManager;
import Model.BookTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class BookManagementPanel extends JPanel {
    private JTable bookTable;
    private BookTableModel bookTableModel;
    private JComboBox<String> searchCriteriaComboBox;
    private JTextField searchField;
    private JButton searchButton;

    public BookManagementPanel() {
        setLayout(new BorderLayout());

        // 创建顶部面板，包含搜索条件下拉框、搜索框和按钮
        JPanel topPanel = new JPanel(new FlowLayout());
        searchCriteriaComboBox = new JComboBox<>(new String[]{"标题", "作者", "出版社"});
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");

        topPanel.add(searchCriteriaComboBox);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // 创建表格模型和表格
        bookTableModel = new BookTableModel();
        bookTable = new JTable(bookTableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);

        // 创建按钮
        JButton addButton = new JButton("增加");
        JButton editButton = new JButton("修改");
        JButton deleteButton = new JButton("删除");

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 添加按钮事件处理
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 显示添加书籍对话框
                BookDialog dialog = new BookDialog(null);
                dialog.setLocationRelativeTo(null); // 设置对话框居中显示
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    Book book = dialog.getBook();
                    try {
                        BookManager.addBook(book);
                        refreshBookList();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookManagementPanel.this, "添加书籍失败");
                    }
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    Book book = bookTableModel.getBookAt(selectedRow);
                    // 显示修改书籍对话框
                    BookDialog dialog = new BookDialog(book);
                    dialog.setLocationRelativeTo(null); // 设置对话框居中显示
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
                        book = dialog.getBook();
                        try {
                            BookManager.updateBook(book);
                            refreshBookList();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(BookManagementPanel.this, "修改书籍失败");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(BookManagementPanel.this, "请选择要修改的书籍");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(BookManagementPanel.this, "确定删除选中的书籍吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        Book book = bookTableModel.getBookAt(selectedRow);
                        try {
                            BookManager.deleteBook(book.getId());
                            refreshBookList();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(BookManagementPanel.this, "删除书籍失败");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(BookManagementPanel.this, "请选择要删除的书籍");
                }
            }
        });

        // 添加搜索按钮的事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText();
                String criteria = (String) searchCriteriaComboBox.getSelectedItem();
                if (keyword.isEmpty()) {
                    refreshBookList();
                } else {
                    try {
                        List<Book> books = BookManager.searchBooks(criteria, keyword);
                        updateTable(books);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookManagementPanel.this, "搜索失败，请稍后重试");
                    }
                }
            }
        });

        // 初始化书籍列表
        refreshBookList();
    }

    private void refreshBookList() {
        try {
            List<Book> books = BookManager.getAllBooks();
            updateTable(books);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "获取书籍列表失败");
        }
    }

    private void updateTable(List<Book> books) {
        bookTableModel.setBooks(books);
    }
}
