import java.awt.*;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable, Comparable {
    private String message;
    private Image image;
    private User sender;
    private User receiver;
    private Date timeStamp;

    public Message(String message, Image image, User sender, User receiver) {
        this.message = message;
        this.image = image;
        this.sender = sender;
        this.receiver = receiver;
        this.timeStamp = new Date(System.currentTimeMillis());
    }

    @Override
    public int compareTo(Object o) {
        return this.timeStamp.compareTo(((Message) o).timeStamp);
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
