import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import Thread.ThreadControl;

public class MainClass {
    public static void main(String[] args) throws IOException {
        int port;
        var scanner = new Scanner(System.in);

        System.out.print("Введите порт для сервера: ");
        port = scanner.nextInt();

        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("Сервер работает\nОжидание подключения");

        while (true) {
            var clientSocket = serverSocket.accept();
            ThreadControl myThread = new ThreadControl(clientSocket);
            myThread.start();
            System.out.println("Новое подключение. IP - " + clientSocket.getInetAddress().toString().replace("/", "") + " Порт - " + clientSocket.getPort());
        }
    }
}
