package Managers;

import Commands.*;

import java.util.Arrays;
import java.util.LinkedList;

public class Commander {
    private CollectionManager collectionManager;
    private LinkedList<ReferenceCommand> commands;

    public Commander(CollectionManager collectionManager) {
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
            return foundCommand.execute(request.getMovie(), request.getArgument());
        } else if (request.getCommand().equals("connect")) {
            return connection();
        } else {
            return new Response("command hasn't been found: " + request.getCommand());
        }
    }
    public void save() {
        collectionManager.save();
        System.out.println("collection saved");
    }
    public Response connection(){
        return new Response("ok");
    }
}
