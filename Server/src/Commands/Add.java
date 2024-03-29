package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class Add extends ReferenceCommand{
    public Add(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if (argument != null) {return new Response("command allows only one argument - Movie-object");}
        else if(obj == null) {return new Response("expected Movie obj, not null");}
        else {
            if (collectionManager.isIdUnique(obj.getId())) {
                collectionManager.add(obj);
            } else {obj.setId(collectionManager.formNewId()); collectionManager.add(obj);}
            return new Response("added " + obj.getName() + " to collection");
        }
    }
}
