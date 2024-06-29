package Entity;

import Entity.User;

public class Student extends User {
    private String studentId;
    private String major;
    private String email;
    private String phoneNumber;

    public Student(int id, String username, String password, String email, String phoneNumber, String studentId, String major) {
        super(id, username, password, "学生");
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.studentId = studentId;
        this.major = major;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getMajor() {
        return major;
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
}