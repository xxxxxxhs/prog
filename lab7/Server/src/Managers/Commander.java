package Managers;

import Commands.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class Commander {
    private CollectionManager collectionManager;
    private LinkedList<ReferenceCommand> commands;
    private SqlUserManager sqlUserManager;

    public Commander(CollectionManager collectionManager, SqlUserManager sqlUserManager) {
        this.collectionManager = collectionManager;
        commands = new LinkedList<ReferenceCommand>(Arrays.asList(
                new Add("add", collectionManager),
                new Clear("clear", collectionManager),
                new CountByGoldenPalmCount("count_by_golden_palm_count", collectionManager),
                new Head("head", collectionManager),
                new Help("help", collectionManager),
                new History("history", collectionManager),
                new Info("info", collectionManager),
                new PrintFieldDescendingMpaaRating("print_field_descending_mpaa_rating", collectionManager),
                new PrintUniqueOscarsCount("print_unique_oscars_count", collectionManager),
                new RemoveById("remove_by_id", collectionManager),
                new RemoveFirst("remove_first", collectionManager),
                new Show("show", collectionManager),
                new Update("update", collectionManager)));
        this.sqlUserManager = sqlUserManager;
    }

    public Response executeCommand(Request request) {
        ReferenceCommand foundCommand = commands.stream()
                .filter(command -> command.getName().equals(request.getCommand()))
                .findFirst()
                .orElse(null);

        if (foundCommand != null) {
            collectionManager.sort();
            if (!foundCommand.getName().equals("history"))
                collectionManager.addToHistory(foundCommand.getName());
            System.out.println("execution " + foundCommand.getName());
            return foundCommand.execute(request.getMovie(), request.getArgument(), request.getUsername());
        } else if (request.getCommand().equals("connect")) {
            return connection();
        } else if (request.getCommand().equals("authorize")) {
            return authorize(request);
        } else {
            return new Response("command hasn't been found: " + request.getCommand());
        }
    }
    /*
    public void save() {
        collectionManager.save();
        System.out.println("collection saved");
    }
    */
    public Response connection(){
        return new Response("ok");
    }
    private Response authorize(Request request) {
        try {
            if (sqlUserManager.checkUserExists(request.getUsername())) {
                if (sqlUserManager.checkPasswordCorrect(request.getUsername(), request.getHashPassword())) {
                    System.out.println("New connection with " + request.getUsername());
                    return new Response("Authorized as " + request.getUsername());
                } else return new Response("Password isn't correct, try again");
            } else {
                sqlUserManager.registerNewUser(request.getUsername(), request.getHashPassword());
                System.out.println("New connection with " + request.getUsername());
                return new Response("Registered as " + request.getUsername());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
