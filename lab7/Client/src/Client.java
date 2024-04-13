import Managers.*;
import Exceptions.IncorrectValueException;
import Managers.CommandResolver;
import Managers.Console;
import Managers.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Client {
    private DatagramChannel channel;
    private final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 1488);
    private String username;
    private String hashPassword;

    public Client() throws IOException {
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false); // Неблокирующий режим
        this.channel.connect(serverAddress); // Подключаемся к серверу
    }
    public boolean checkConnection() {
        try {
            send(CommandResolver.connect());
            return true;
        } catch (PortUnreachableException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }
    private void waitForConnection(boolean start) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (start) {
            System.out.println("Сервер доступен. Можно вводить команды.");
            return;
        }
        while (true) {
            System.out.println("Сервер недоступен. Введите 'connect' для повторного подключения.");
            String input = reader.readLine();
            if (input.equals("connect") && checkConnection()) {
                System.out.println("Сервер доступен. Можно вводить команды.");
                break;
            }
        }
    }
    public void readUserCommand() throws IOException {
        waitForConnection(checkConnection()); // Ожидаем доступности сервера перед началом ввода команд
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while (!(line = reader.readLine()).equals("exit")) {
            if (!checkConnection()) {
                waitForConnection(checkConnection());
            }
            String[] args = line.split(" ");
            if (!args[0].equals("execute_script")) {
                try {
                    Request request = CommandResolver.resolve(args, username, hashPassword);
                    if (request != null && !request.getCommand().equals("connect")) send(request);
                } catch (IncorrectValueException e) {
                    throw new RuntimeException(e);
                }
            } else {
                LinkedList<Request> requests = CommandResolver.executeScript(args[1], username, hashPassword);
                for (Request request : requests) {
                    send(request);
                }
            }
        }
    }
    public Response send(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        byte[] data = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.write(buffer);
        return waitForResponse();
    }
    public Response waitForResponse() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(65535);
        while (channel.read(buffer) <= 0) {
            try {
                // Небольшая задержка для уменьшения нагрузки на CPU
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Восстановление прерванного статуса
                System.err.println("Ожидание было прервано");
                return null;
            }
        }

        buffer.flip();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Response response = (Response) ois.readObject();
            if (!response.getAnswer().equals("ok")) {
                System.out.println(response.getAnswer());
                return response;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void authenticateUser() throws NoSuchAlgorithmException {
        Console console = new Console();
        console.print("Enter username: ");
        String username = console.readln();
        String password;

        while (true) {
            console.print("Enter password: ");
            password = console.readln();
            Request request = new Request("authorize", null, null, username, Encryptor.getHash(password));
            Response response = null;
            try {
                response = send(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (response.getAnswer().equals("Authorized as " + request.getUsername()) ||
                    response.getAnswer().equals("Registered as " + request.getUsername())) {
                this.username = username;
                try {
                    this.hashPassword = Encryptor.getHash(password);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}