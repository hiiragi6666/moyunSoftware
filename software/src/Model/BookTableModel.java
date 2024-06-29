package Model;

import Entity.Book;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BookTableModel extends AbstractTableModel {
    private List<Book> books;
    private String[] columnNames = {"ID", "标题", "作者", "出版社", "年份", "ISBN", "类别", "描述"};

    public BookTableModel() {
        books = new ArrayList<>();
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        fireTableDataChanged();
    }

    public Book getBookAt(int rowIndex) {
        return books.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.getId();
            case 1:
                return book.getTitle();
            case 2:
                return book.getAuthor();
            case 3:
                return book.getPublisher();
            case 4:
                return book.getYear();
            case 5:
                return book.getIsbn();
            case 6:
                return book.getCategory();
            case 7:
                return book.getDescription();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}