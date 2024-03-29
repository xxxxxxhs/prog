import Managers.Commander;
import Managers.Request;
import Managers.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    Commander commander;
    DatagramSocket datagramSocket;

    public Server(Commander commander, DatagramSocket datagramSocket) {
        this.commander = commander;
        this.datagramSocket = datagramSocket;
    }

    public void run() throws IOException {
        byte[] buf = new byte[65536];
        DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
        datagramSocket.receive(receivePacket);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength());
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            // Десериализация объекта
            Request request = (Request) ois.readObject();
            Response response = commander.executeCommand(request);


            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                // Сериализация объекта
                oos.writeObject(response);
                byte[] data = baos.toByteArray();

                // Создание и отправка DatagramPacket
                DatagramPacket packet = new DatagramPacket(data, data.length, receivePacket.getAddress(), receivePacket.getPort());
                datagramSocket.send(packet);
                datagramSocket.close();
            } catch (IOException e) {
                System.out.println("smth wrong with the stream");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Переданного объекта не существует в серверной части");
        }
    }
}
