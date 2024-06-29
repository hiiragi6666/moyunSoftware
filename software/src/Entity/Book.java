package Entity;

public class Book {
    private int id;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private int year;
    private String isbn;
    private String category;

    public Book(int id, String title, String author, String description, String publisher, int year, String isbn, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.category = category;
    }

    // Getter and setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return title;
    }
}
