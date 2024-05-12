import CollectionClasses.*;
import Exceptions.IncorrectValueException;
import Interface.FrameManager;
import Interface.MainFrame;
import Managers.Client;
import Managers.Encryptor;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws IncorrectValueException {
        try {
            Client client = new Client();
            FrameManager frameManager = new FrameManager(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}