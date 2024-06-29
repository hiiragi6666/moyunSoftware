package Entity;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phoneNumber;

    public User(int id, String username, String password, String role, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(int id, String username, String password, String role) {
        this(id, username, password, role, null, null);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdmin() {
        return "管理员".equals(this.role);
    }
}