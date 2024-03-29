package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class Update extends ReferenceCommand{
    public Update(String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }
    @Override
    public Response execute(Movie obj, String argument) {
        if (obj == null || argument == null) {return new Response("expected 2 arguments: id and object");}
        else {
            long id;
            try {
                id = Long.parseLong(argument);
            } catch (NumberFormatException e) {
                return new Response("wrong id, expected number");
            }
            if (collectionManager.isExistById(id)) {
                collectionManager.updateById(obj, id);
                return new Response("updated element " + id + ": " + obj.getName());
            } else {return new Response("element with such id doesn't exists");}
        }
    }
}
