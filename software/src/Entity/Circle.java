package Entity;
import Manager.CircleManager;

import java.util.List;

public class Circle {
    private int id;
    private String name;
    private int creatorId;

    public Circle(int id, String name, int creatorId) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    

    public int getCreatorId() {
        return creatorId;
    }

    // 加载圈子消息
    public List<String> loadMessages() {
        try {
            return CircleManager.getCircleMessages(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 发布消息到圈子
    public boolean postMessage(int userId, String message) {
        try {
            CircleManager.postMessageToCircle(id, userId, message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public String toString() {
        return this.name;
    }
}