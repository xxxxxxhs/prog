import Managers.Request;
import Managers.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    DatagramSocket datagramSocket;
    int port;
    InetAddress adress;

    public Client(DatagramSocket datagramSocket, int port, InetAddress adress) {
        this.datagramSocket = datagramSocket;
        this.port = port;
        this.adress = adress;
    }

    public void send(Request request) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            // Сериализация объекта
            oos.writeObject(request);
            byte[] data = baos.toByteArray();
            System.out.println(request.toString());

            InetAddress address = InetAddress.getByName("localhost");
            // Создание и отправка DatagramPacket
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            datagramSocket.send(packet);
            recieve();
        } catch (IOException e) {
            System.out.println("smth wrong with the stream");
        }
    }

    private void recieve() throws IOException {
        byte[] buf = new byte[65536];
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        datagramSocket.receive(datagramPacket);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(datagramPacket.getData(), 0, datagramPacket.getLength());
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            Response response = (Response) ois.readObject();
            System.out.println(response.getAnswer());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

