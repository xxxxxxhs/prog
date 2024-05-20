package Interface;

import CollectionClasses.Movie;
import Managers.Client;
import Managers.Request;
import Managers.Response;

import javax.swing.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FrameManager implements RegistrationListener{
    private Client client;
    private SignUpWindow signUpWindow;
    private MainFrame mainFrame;
    private ScheduledFuture<?> pollingFuture;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public FrameManager(Client client) {
        this.client = client;
        signUpWindow = new SignUpWindow(client);
        start();
    }
    public void start() {
        signUpWindow.setRegistrationListener(this);
        signUpWindow.initializeUI();
    }

    @Override
    public void onRegistrationSuccess(String username, String password) {
        // Запуск MainFrame с полученными данными
        signUpWindow.dispose();
        mainFrame = new MainFrame(username, password, client, this);
        mainFrame.initializeUI();
        startPolling();
        //getActualCollection();
    }
    public void startPolling() {
        if (pollingFuture == null || pollingFuture.isDone()) {
            Runnable pollingTask = createPollingTask();
            pollingFuture = scheduler.scheduleAtFixedRate(pollingTask, 0, 2, TimeUnit.SECONDS);
            System.out.println("Polling started");
        }
    }

    public void stopPolling() {
        if (pollingFuture != null && !pollingFuture.isCancelled()) {
            pollingFuture.cancel(true); // Прерывание текущей задачи поллинга
            System.out.println("Polling stopped immediately");
        }
    }
    private Runnable createPollingTask() {
        return () -> {
            try {
                Response response = client.send(new Request("show", null, null, client.getUsername(), client.getHashPassword()));
                if (response.isContainCollection()) {
                    LinkedList<Movie> collection = response.getCollection();
                    LinkedList<Movie> oldCollection = mainFrame.getCollection();
                    Collections.sort(oldCollection);
                    Collections.sort(collection);
                    if (!areListsEqual(collection, oldCollection)) {
                        mainFrame.updateTable(collection);
                        System.out.println("Collection updated, new collection size: " + collection.size());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
    private boolean areListsEqual(LinkedList<Movie> list1, LinkedList<Movie> list2) {
        HashSet<Movie> set1 = new HashSet<>(list1);
        HashSet<Movie> set2 = new HashSet<>(list2);
        return set1.equals(set2);
    }

}
