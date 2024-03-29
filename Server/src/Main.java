import Managers.CollectionManager;
import Managers.Commander;
import Managers.Console;
import Managers.Dumper;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, SocketException {
        String fileName = "/Users/fqy/edu/prog/lab6/movies.json";
        Dumper dumper = new Dumper(fileName, new Console());
        Commander commander = new Commander(new CollectionManager(dumper));
        try {
            while (true) {
                new Server(commander, new DatagramSocket(1488, InetAddress.getLocalHost())).run();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}