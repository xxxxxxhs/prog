import Managers.Commander;
import Managers.Request;
import Managers.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class Server {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[4096];
    private Commander commander;

    private ForkJoinPool readPool = ForkJoinPool.commonPool();
    private ExecutorService processPool = Executors.newFixedThreadPool(10);
    private ExecutorService sendPool = Executors.newCachedThreadPool();

    public Server(Commander commander) throws SocketException {
        this.socket = new DatagramSocket(1488); // Порт можно настроить
        this.commander = commander;
    }

    public void start() {
        running = true;
        while (running) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
                readPool.execute(() -> handlePacket(packet));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private void handlePacket(DatagramPacket packet) {
        // Десериализация запроса из пакета
        Request request = deserializeRequest(packet.getData());

        processPool.submit(() -> {
            Response response = processRequest(request);
            sendResponse(response, packet.getAddress(), packet.getPort());
        });
    }

    private Request deserializeRequest(byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Request) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Response processRequest(Request request) {
        return commander.executeCommand(request);
    }

    private void sendResponse(Response response, InetAddress address, int port) {
        sendPool.execute(() -> {
            byte[] responseData = serializeResponse(response);
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, port);
            try {
                socket.send(responsePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private byte[] serializeResponse(Response response) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
