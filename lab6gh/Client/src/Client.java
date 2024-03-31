import Exceptions.IncorrectValueException;
import Managers.CommandResolver;
import Managers.Request;
import Managers.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;

public class Client {
    private DatagramChannel channel;
    private final InetSocketAddress serverAddress = new InetSocketAddress("localhost", 1488);

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
                    Request request = CommandResolver.resolve(args);
                    if (request != null && !request.getCommand().equals("connect")) send(request);
                } catch (IncorrectValueException e) {
                    throw new RuntimeException(e);
                }
            } else {
                LinkedList<Request> requests = CommandResolver.executeScript(args[1]);
                for (Request request : requests) {
                    send(request);
                }
            }
        }
    }
    public void send(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        byte[] data = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.write(buffer);
        waitForResponse();
    }
    public void waitForResponse() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(65535);
        while (channel.read(buffer) <= 0) {
            try {
                // Небольшая задержка для уменьшения нагрузки на CPU
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Восстановление прерванного статуса
                System.err.println("Ожидание было прервано");
                return;
            }
        }

        buffer.flip();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            Response response = (Response) ois.readObject();
            if (!response.getAnswer().equals("ok")) {
                System.out.println(response.getAnswer());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

