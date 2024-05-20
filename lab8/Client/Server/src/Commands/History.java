package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class History extends ReferenceCommand {
    public History(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null || argument != null) {return new Response("command doesn't allow arguments");}
        else {return new Response(collectionManager.getHistory());}
    }
}
