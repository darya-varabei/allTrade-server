package ConnectionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionManager {
    private Socket clientSocket;
    private ObjectOutputStream sendMessage;
    private ObjectInputStream readMessage;

    public ConnectionManager(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            sendMessage = new ObjectOutputStream(clientSocket.getOutputStream());
            readMessage = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object object) {
        try {
            sendMessage.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendString(String text) { sendObject(text); }

    public Object readObject() {
        try {
            return readMessage.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String readString() { return (String)this.readObject(); }

    public void Close() throws IOException {
        sendMessage.close();
        readMessage.close();
        clientSocket.close();
    }
}
