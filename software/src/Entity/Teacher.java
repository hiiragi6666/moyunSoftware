package Entity;

public class Teacher extends User {
    private String teacherId;
    private String department;
    private String email;
    private String phoneNumber;

    public Teacher(int id, String username, String password, String email, String phoneNumber, String teacherId, String department) {
        super(id, username, password, "教师");
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.teacherId = teacherId;
        this.department = department;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}