package Thread;

import java.io.IOException;
import java.net.Socket;
import ConnectionManager.ConnectionManager;
import ServerController.ServerController;

public class ThreadControl extends Thread{
    private Socket clientSocket;

    public ThreadControl(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        var manager = new ConnectionManager(clientSocket);
        ServerController serverController = new ServerController(manager);

        try {
            serverController.work();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
