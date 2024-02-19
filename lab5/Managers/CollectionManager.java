package Managers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import CollectionClasses.Movie;
import CollectionClasses.MpaaRating;

/*
 * class Collection manager is need to manage linked list of Movie
 */

public class CollectionManager {
    
    private LocalDateTime initTime;
    private LinkedList<Movie> collection;
    private Console console;
    private Dumper dumper;

    public CollectionManager(Dumper dumper, Console console) {
        this.collection = dumper.load();
        this.initTime = LocalDateTime.now();
        this.console = console;
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
            elements += mv.toString() + "\n";
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
            console.printError("element with this id doesn't exist");
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
    private Boolean isExistById(long id){
        for (Movie mv : collection) {
            if (mv.getId() == id) {return true;}
        }
        return false;
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
    public void removeById(long id) {
        if (getById(id) == null) {
            console.printError("element with this id doesn't exist");
        } else {
            collection.remove(getById(id));
        }
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
    public void removeFirst() {
        collection.removeFirst();
    }

    /*
     * @return first element of the collection
     */
    public Movie head() {
        return collection.getFirst();
    }

    /*
     * @param goldenPalmCount
     * @returns amount of collection elements with specified value of goldenPalmCount
     */
    public int countByGoldenPalmCount(long goldenPalmCount) {
        int counter = 0;
        for (Movie mv : collection) {
            if (mv.getGoldenPalmCount() == goldenPalmCount) {
                counter++;
            }
        }
        return counter;
    }

    /*
     * @return string of unique values of oscarsCount
     */
    public String printUniqueOscarsCount() {
        ArrayList<Integer> lis = new ArrayList<>();
        for (Movie mv : collection) {
            lis.add(mv.getOscarsCount());
        }
        int counter = 0;
        String answer = "";
        for (Integer i : lis) {
            for (Integer j : lis) {
                if (i == j){
                    counter++;
                }
            }
            if (counter == 1) {answer += i.toString() + "\n";}
            counter = 0;
        }
        return answer;
    }

    /*
     * @return string of descending values of mpaa rating of existing collection elements
     */
    public String printFieldDescendingMpaaRating() {
        LinkedList<MpaaRating> lis = new LinkedList<>();
        for (Movie mv : collection) {
            lis.add(mv.getMpaaRating());
        }
        Collections.sort(lis);
        Collections.reverse(lis);
        String answer = "";
        for (MpaaRating rate : lis) {
            answer += rate.toString() + "\n";
        }
        return answer;
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
}
