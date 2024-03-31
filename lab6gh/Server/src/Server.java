import Managers.Commander;
import Managers.Request;
import Managers.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

public class Server {
    private Selector selector;
    private DatagramChannel channel;
    private Commander commander;

    public Server(Commander commander) throws IOException {
        this.selector = Selector.open(); // Открываем селектор
        this.channel = DatagramChannel.open(); // Открываем датаграммный канал
        this.channel.configureBlocking(false); // Настройка канала на неблокирующий режим
        this.channel.socket().bind(new InetSocketAddress(1488)); // Привязываем канал к порту
        this.channel.register(selector, SelectionKey.OP_READ); // Регистрируем канал в селекторе на операцию чтения
        this.commander = commander;

        new Thread(this::readConsoleCommands).start();
    }

    public void receive() throws IOException, ClassNotFoundException {
        while (true) {
            selector.select(); // Блокируется до тех пор, пока не будет хотя бы один канал готов к I/O операции
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove(); // Удаляем обработанный ключ из итератора

                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(65535); // Буфер для чтения данных
                    SocketAddress clientAddress = channel.receive(buffer); // Чтение данных из канала
                    buffer.flip(); // Переводим буфер в режим чтения

                    // Десериализация полученного объекта Request из байтов
                    ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Request request = (Request) ois.readObject();

                    // Обработка полученного запроса и получение ответа
                    Response response = commander.executeCommand(request);

                    // Отправка ответа клиенту
                    send(response, clientAddress);
                }
            }
        }
    }

    public void send(Response response, SocketAddress clientAddress) throws IOException {
        // Сериализация Response в байты
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());

        channel.send(buffer, clientAddress);
        buffer.clear();
    }

    private void readConsoleCommands() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("save".equalsIgnoreCase(line.trim())) {
                    commander.save(); // Вызываем метод save у commander
                } else if ("exit".equals(line)) {
                    System.out.println("shut down...");
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            System.out.println("shut down...");
            System.exit(-1);
        }
    }
}

