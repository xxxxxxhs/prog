package Managers;

import CollectionClasses.Movie;
import Exceptions.EmptyCollection;
import Exceptions.IdDoesntExists;

import java.time.LocalDateTime;
import java.util.*;

/*
 * class Collection manager is need to manage linked list of Movie
 */

public class CollectionManager {
    
    private LocalDateTime initTime;
    private LinkedList<Movie> collection;
    private Dumper dumper;
    private LinkedList<String> history = new LinkedList<>();

    public CollectionManager(Dumper dumper) {
        List<Movie> loadedMovies = dumper.load();
        collection = new LinkedList<>();
        loadedMovies.forEach(movie -> {
            if (movie.validate()) {
                collection.add(movie);
            } else {
                System.out.println("Фильм с ID " + movie.getId() + " не прошел валидацию.");
            }
        });
        this.initTime = LocalDateTime.now();
        this.dumper = dumper;
        Collections.sort(this.collection);
    }

    /*
     * @return text of info-command
     */
    public String getInfo() {
        return "Init date: " + initTime + "\n" +
        "Collection type is Linked list of movies\n" +
        "Contains " + collection.size() + (collection.size() == 1 ? " element" : " elements");
    }

    /*
     * @return elements of collection
     */
    public String show() {
        String elements = "";
        for (Movie mv : collection) {
            elements += mv.toString() + "----------------\n";
        }
        return elements;
    }

    /*
     * @param mv    new Movie element
     * adds new Movie generated with Ask.class into the collection
     */
    public void add(Movie mv) {
        if (!isIdUnique(mv.getId())) {mv.setId(formNewId());}
        collection.add(mv);
    }

    /* 
     * @param mv    new Movie-element
     * @param id    id of old Movie-element
     * updates Movie-element with Ask.class according to its id
     */
    public void updateById(Movie mv, long id) {
        if (getById(id) == null) {
            //console.printError("element with this id doesn't exist");
        } else {
            collection.remove(getById(id));
            mv.setId(id);
            collection.add(mv);
        }
    }

    /*
     * @param id
     * @return bool-value if element with such id exists
     */
    public boolean isExistById(long id) {
        return collection.stream().anyMatch(movie -> movie.getId() == id);
    }

    /*
     * @param id
     * @return collection-element with specified id
     */
    private Movie getById(long id) {
        for (Movie mv : collection) {
            if (mv.getId() == id) {return mv;}
        }
        return null;
    }

    /*
     * @param id
     * removes element with specified id
     */
    public void removeById(long id) throws EmptyCollection, IdDoesntExists{
        if (collection == null || collection.isEmpty()) {
            throw new EmptyCollection("collection is empty");
        }
        if (!isExistById(id)) {
            throw new IdDoesntExists("Such id doesn't exists");
        }
        collection.removeIf(movie -> movie.getId() == id);
    }

    /*
     * clears collection
     */
    public void clear() {
        collection.clear();
    }

    /*
     * saves collection into json-file
     */
    public void save() {
        dumper.save(collection);
    }

    /*
     * removes first element of the collection
     */
    public void removeFirst() throws EmptyCollection {
        if (collection.size() != 0) {
            collection.removeFirst();
        } else {throw new EmptyCollection("collection is already empty");}
        // else {console.printError("collection is empty");}
    }

    /*
     * @return first element of the collection
     */
    public Movie head() throws EmptyCollection {
        try {
            return collection.getFirst();
        } catch (NoSuchElementException e) {
            throw new EmptyCollection("collection is empty");
            //console.printError("collection is empty");
        }
    }

    /*
     * @param goldenPalmCount
     * @returns amount of collection elements with specified value of goldenPalmCount
     */
    public int countByGoldenPalmCount(long goldenPalmCount) {
        return (int) collection.stream()
                .filter(movie -> movie.getGoldenPalmCount() == goldenPalmCount)
                .count();
    }

    /*
     * @return string of unique values of oscarsCount
     */
    public String printUniqueOscarsCount() {
        String[] uniqueOscarsCounts = collection.stream()
                .filter(movie -> Objects.nonNull(movie.getOscarsCount()))
                .map(Movie::getOscarsCount)
                .distinct()
                .map(Object::toString)
                .toArray(String[]::new);

        return String.join("\n", uniqueOscarsCounts);
    }

    /*
     * @return string of descending values of mpaa rating of existing collection elements
     */
    public String printFieldDescendingMpaaRating() {
        String[] ratings = collection.stream()
                .map(Movie::getMpaaRating)
                .sorted((r1, r2) -> r2.compareTo(r1))
                .map(Enum::name)
                .toArray(String[]::new);

        return String.join("\n", ratings);
    }

    /*
     * @param id
     * @return bool-value if specified id unique or not
     */
    public Boolean isIdUnique(long id) {
        for (Movie mv : collection) { if (mv.getId() == id) {return false;}}
        return true;
    }

    /*
     * @return new unique id
     */
    public long formNewId() {
        long id = 1;
        while (!isIdUnique(id)) {id++;}
        return id;
    }
    public void sort() {
        Collections.sort(collection);
    }
    public void addToHistory(String command) {
        if (history.size() < 13) {history.add(command);}
        else {history.removeFirst(); history.add(command);}
    }
    public String getHistory(){return String.join("\n", history.toArray(new String[0]));}
}
