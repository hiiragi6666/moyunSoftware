package UI;

import Entity.Book;
import Manager.BookManager;
import Entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class BookLibraryPanel extends JPanel {
    private JList<Book> bookList;
    private JTextArea bookDetail;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox<String> searchCriteriaComboBox;
    private JButton viewBookButton; // 新增的按钮
    private JButton reviewBookButton; // 新增的按钮
    private JButton reviewBookButton1; // 新增的按钮
    private JButton deleteReviewButton; // 新增的按钮
    private User user;

    public BookLibraryPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());

        // 创建搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchCriteriaComboBox = new JComboBox<>(new String[]{"标题", "作者", "出版社"});
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");

        searchPanel.add(searchCriteriaComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 创建书名列表和书籍详情面板
        bookList = new JList<>();
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.addListSelectionListener(e -> showBookDetail(bookList.getSelectedValue()));

        JScrollPane listScrollPane = new JScrollPane(bookList);
        listScrollPane.setPreferredSize(new Dimension(200, 0)); // 设置书名列表的宽度

        bookDetail = new JTextArea();
        bookDetail.setEditable(false);
        JScrollPane detailScrollPane = new JScrollPane(bookDetail);

        // 添加查看书籍按钮
        viewBookButton = new JButton("查看书籍");
        viewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 在这里可以不做任何操作，因为书籍详情已经在选择书籍时显示
            }
        });

        // 新增评价书籍按钮
        reviewBookButton = new JButton("评价书籍");
        reviewBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    String review = JOptionPane.showInputDialog(BookLibraryPanel.this, "请输入对书籍的评价:", "评价书籍", JOptionPane.PLAIN_MESSAGE);
                    if (review != null && !review.trim().isEmpty()) {
                        try {
                            String username = user.getUsername();
                            BookManager.saveUserReview(username, selectedBook.getTitle(), review);
                            showBookDetail(selectedBook); // 更新显示的书籍详情
                            JOptionPane.showMessageDialog(BookLibraryPanel.this, "评价已保存: " + review, "信息", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(BookLibraryPanel.this, "保存评价时出错", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(BookLibraryPanel.this, "请先选择一本书", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 新增查看所有书评按钮
        reviewBookButton1 = new JButton("查看所有书评");
        reviewBookButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    try {
                        List<String[]> comments = BookManager.getBookReviews(selectedBook.getTitle());
                        if (!comments.isEmpty()) {
                            StringBuilder reviewsText = new StringBuilder();
                            for (String[] comment : comments) {
                                String username = comment[0]; // 假设用户名在数组的第一个位置
                                String reviewText = comment[1]; // 假设评论内容在数组的第二个位置
                                reviewsText.append("用户  ").append(username).append("：").append(reviewText).append("\n\n");
                            }
                            JOptionPane.showMessageDialog(BookLibraryPanel.this, reviewsText.toString(), "所有书评", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(BookLibraryPanel.this, "该书暂无书评", "提示", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookLibraryPanel.this, "获取书评时出错", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookLibraryPanel.this, "请先选择一本书", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // 新增删除书评按钮
        deleteReviewButton = new JButton("删除我的书评");
        deleteReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    try {
                        List<String[]> comments = BookManager.getBookReviews(selectedBook.getTitle());
                        if (!comments.isEmpty()) {
                            if (user.isAdmin()) {
                                // 管理员可以看到并删除所有评论
                                String[] allReviews = comments.stream()
                                        .map(comment -> comment[0] + ": " + comment[1])
                                        .toArray(String[]::new);
                                String selectedReview = (String) JOptionPane.showInputDialog(
                                        BookLibraryPanel.this,
                                        "请选择要删除的书评：",
                                        "删除书评",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        allReviews,
                                        allReviews[0]
                                );
                                if (selectedReview != null) {
                                    String[] parts = selectedReview.split(": ", 2);
                                    String username = parts[0];
                                    String review = parts[1];
                                    BookManager.deleteUserReview(username, selectedBook.getTitle(), review);
                                    showBookDetail(selectedBook);
                                    JOptionPane.showMessageDialog(BookLibraryPanel.this, "书评已删除", "信息", JOptionPane.INFORMATION_MESSAGE);
                                }
                            } else {
                                // 普通用户只能删除自己的评论,保持原有逻辑
                                String username = user.getUsername();
                                String[] userReviews = comments.stream()
                                        .filter(comment -> comment[0].equals(username))
                                        .map(comment -> comment[1])
                                        .toArray(String[]::new);
                                // ... 原有的普通用户删除逻辑 ...
                                String selectedReview = (String) JOptionPane.showInputDialog(
                                        BookLibraryPanel.this,
                                        "请选择要删除的书评：",
                                        "删除书评",
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        userReviews,
                                        userReviews[0]
                                );
                                if (selectedReview != null) {
                                    BookManager.deleteUserReview(username, selectedBook.getTitle(), selectedReview);
                                    showBookDetail(selectedBook); // 更新显示的书籍详情
                                    JOptionPane.showMessageDialog(BookLibraryPanel.this, "书评已删除", "信息", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(BookLibraryPanel.this, "该书暂无书评", "提示", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(BookLibraryPanel.this, "删除书评时出错", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookLibraryPanel.this, "请先选择一本书", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewBookButton);
        buttonPanel.add(reviewBookButton); // 将评价书籍按钮添加到按钮面板
        buttonPanel.add(reviewBookButton1);
        buttonPanel.add(deleteReviewButton); // 将删除书评按钮添加到按钮面板

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, detailScrollPane);
        splitPane.setDividerLocation(200); // 设置分割条位置，调整初始比例
        splitPane.setResizeWeight(0.3); // 设置分割条调整后的比例

        add(searchPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH); // 将按钮面板添加到南部

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                String criteria = (String) searchCriteriaComboBox.getSelectedItem();
                searchBooks(criteria, query);
            }
        });

        loadBooks();
    }

    private void loadBooks() {
        try {
            List<Book> books = BookManager.getAllBooks();
            bookList.setListData(books.toArray(new Book[0]));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "无法加载书籍信息", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBooks(String criteria, String query) {
        try {
            List<Book> books = BookManager.searchBooks(criteria, query);
            bookList.setListData(books.toArray(new Book[0]));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "搜索书籍时出错", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBookDetail(Book book) {
        if (book != null) {
            bookDetail.setText("标题: " + book.getTitle() + "\n\n" +
                    "作者: " + book.getAuthor() + "\n\n" +
                    "出版社: " + book.getPublisher() + "\n\n" +
                    "出版年份: " + book.getYear() + "\n\n" +
                    "ISBN: " + book.getIsbn() + "\n\n" +
                    "类别: " + book.getCategory() + "\n\n" +
                    "描述:\n" + book.getDescription());
        } else {
            bookDetail.setText("");
        }
    }
}