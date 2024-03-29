import Exceptions.IncorrectValueException;
import Managers.CommandResolver;
import Managers.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class Main {

    public static void main(String[] args) throws UnknownHostException, SocketException {
    Client client = new Client(new DatagramSocket(), 1488, InetAddress.getLocalHost());
        askCommand(client);
    }

    private static void askCommand(Client client) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String[] commandAndArgs;
            commandAndArgs = reader.readLine().split(" ");
            do {
                /*
                if (commandAndArgs[0].equals("execute_script")) {
                    File file = new File(commandAndArgs[1]);
                    try (var reader = new BufferedReader(new FileReader(commandAndArgs[1]))) {
                    }catch (Exception e) {e.printStackTrace();}
                }
                */

                Request request = CommandResolver.resolve(commandAndArgs);
                if (request == null) {
                    System.out.println("wrong command");
                } else if (request.getCommand().equals("execute_script")) {
                    LinkedList<Request> listOfRequests = CommandResolver.executeScript(request.getArgument());
                    for (Request req : listOfRequests) {client.send(req);}
                } else {
                    client.send(request);
                }
            } while (!commandAndArgs[0].equals("exit"));
        } catch (IOException e) {
            throw new RuntimeException("Что то пошло не так! ");
        } catch (IncorrectValueException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            System.out.println("EOF");
            System.exit(-1);
        }
    }

}