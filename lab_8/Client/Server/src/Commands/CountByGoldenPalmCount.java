package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class CountByGoldenPalmCount extends ReferenceCommand{
    public CountByGoldenPalmCount(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null) {return new Response("expected 1 argument: id");}
        else {
            try{
                long goldenPalmCount = Long.parseLong(argument);
                return new Response(String.valueOf(collectionManager.countByGoldenPalmCount(goldenPalmCount)));
            } catch (NumberFormatException e) {return new Response("golden_palm_count should be a number");}
        }
    }
}
