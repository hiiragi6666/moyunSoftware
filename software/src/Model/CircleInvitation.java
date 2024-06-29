package Model; //

public class CircleInvitation {
    private int id;
    private int circleId;
    private int studentId;
    private String status;
    private String inviterName;
    private String circleName;

    // 构造函数
    public CircleInvitation(int id, int circleId, int studentId, String status, String inviterName, String circleName) {
        this.id = id;
        this.circleId = circleId;
        this.studentId = studentId;
        this.status = status;
        this.inviterName = inviterName;
        this.circleName = circleName;
    }

    // Getters
    public String getInviterName() {
        return inviterName;
    }

    public String getCircleName() {
        return circleName;
    }

    public int getId() {
        return id;
    }

    public int getCircleId() {
        return circleId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    // 可选：添加 toString() 方法，方便在列表中显示
    @Override
    public String toString() {
        return "CircleInvitation{" +
               "id=" + id +
               ", circleId=" + circleId +
               ", studentId=" + studentId +
               ", status='" + status + '\'' +
               '}';
    }
}