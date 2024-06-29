package Manager;

import Entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookManager {
    private static final String URL = "jdbc:mysql://localhost:3306/user";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // 修改为你的数据库密码

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Books")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                String publisher = resultSet.getString("publisher");
                int year = resultSet.getInt("year");
                String isbn = resultSet.getString("isbn");
                String category = resultSet.getString("category");
                String comment = resultSet.getString("comment");
                books.add(new Book(id, title, author, description, publisher, year, isbn, category));
            }
        }
        return books;
    }

    public static List<Book> searchBooks(String criteria, String keyword) throws SQLException {
        String query = switch (criteria) {
            case "标题" -> "SELECT * FROM books WHERE title LIKE ?";
            case "作者" -> "SELECT * FROM books WHERE author LIKE ?";
            case "出版社" -> "SELECT * FROM books WHERE publisher LIKE ?";
            default -> "";
        };
        List<Book> books = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            String searchKeyword = "%" + keyword + "%";
            statement.setString(1, searchKeyword);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("description"),
                            rs.getString("publisher"),
                            rs.getInt("year"),
                            rs.getString("isbn"),
                            rs.getString("category")
                    );
                    books.add(book);
                }
            }
        }
        return books;
    }
    // 添加书籍
    public static void addBook(Book book) throws SQLException {
        String query = "INSERT INTO books (id, title, author, publisher, year, isbn, category, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, book.getId());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getYear());
            statement.setString(6, book.getIsbn());
            statement.setString(7, book.getCategory());
            statement.setString(8, book.getDescription());

            statement.executeUpdate();
        }
    }

    // 更新书籍
    public static void updateBook(Book book) throws SQLException {
        String query = "UPDATE books SET title = ?, author = ?, publisher = ?, year = ?, isbn = ?, category = ?, description = ? WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getPublisher());
            statement.setInt(4, book.getYear());
            statement.setString(5, book.getIsbn());
            statement.setString(6, book.getCategory());
            statement.setString(7, book.getDescription());
            statement.setInt(8, book.getId());

            statement.executeUpdate();
        }
    }

    // 删除书籍
    public static void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }

    public static void saveUserReview(String username, String title, String comment) throws SQLException {
        String sql = "INSERT INTO ub (username, title, comment) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, title);
            stmt.setString(3, comment);
            stmt.executeUpdate();
        }
    }

    // 删除书评的方法
    public static void deleteUserReview(String username, String title, String comment) throws SQLException {
        String query = "DELETE FROM ub WHERE username = ? AND title = ? AND comment = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, title);
            statement.setString(3, comment);
            statement.executeUpdate();
        }
    }

    public static List<String[]> getBookReviews(String title) throws SQLException {
        List<String[]> comments = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT username, comment FROM ub WHERE title = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, title);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String username = rs.getString("username");
                        String comment = rs.getString("comment");
                        String[] commentArray = {username, comment};
                        comments.add(commentArray); // Add username and comment as String[]
                    }
                }
            }
        }

        return comments;
    }

}