import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.*;

public class User extends Thread {
    private UUID uuid;
    private Socket socket;
    private Set<User> contacts;
    private List<Message> sentMessages;
    private List<Message> receivedMessages;
    private String username;
    private Icon profilePicture;
    ObjectOutputStream oos;

    public User(String username, Icon profilePicture, String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            oos = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.receivedMessages = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.contacts = new HashSet<>();
        uuid = UUID.randomUUID();
        this.username = username;
        this.profilePicture = profilePicture;

        checkFile();
    }

    public void checkFile() {
        File dirr = new File("C:\\client_chat");

        if (!dirr.exists()) {
            dirr.mkdirs();
        } else {
            File obj = new File(dirr, "info.txt");
            if (obj.exists()) {
                try {
                    FileInputStream fi = new FileInputStream(obj);
                    ObjectInputStream oi = new ObjectInputStream(fi);

                    this.setContacts(((User) oi.readObject()).getContacts());
                    this.setReceivedMessages(((User) oi.readObject()).receivedMessages);
                    this.setSentMessages(((User) oi.readObject()).sentMessages);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            while (!this.isInterrupted()) {
                if (ois.available() == 0)
                    return;

                Message message = (Message) ois.readObject();
                receivedMessages.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        saveUser();
    }

        public List<Message> getMessagesFromUserInOrder(User user) {
        List<Message> messages = new ArrayList<>();
        for (Message message : getReceivedMessages()) {
            if (message.getSender() == user)
                messages.add(message);
        }
        for (Message message : getSentMessages()) {
            if (message.getReceiver() == user)
                messages.add(message);
        }

        Collections.sort(messages);

        return messages;
    }

    public void sendMessage(User user, String text, Image image) throws IOException {
        oos.writeObject(new Message(text, image, this, user));
        oos.flush();
    }

    public void saveUser() {
        try {
            FileOutputStream fileOut = new FileOutputStream("C:\\client_chat");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addContact(User user) {
        contacts.add(user);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Set<User> getContacts() {
        return contacts;
    }

    public void setContacts(Set<User> contacts) {
        this.contacts = contacts;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Icon getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Icon profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }
}
