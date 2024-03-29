package Commands;

import CollectionClasses.Movie;
import Managers.Response;

public interface Executable {
    public Response execute(Movie obj, String argument);
}
