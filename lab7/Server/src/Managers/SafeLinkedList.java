package Managers;

import CollectionClasses.Movie;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class SafeLinkedList<E> {
    private LinkedList<Movie> collection = new LinkedList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public int size() {
        lock.lock();
        try {
            return collection.size();
        } finally {
            lock.unlock();
        }
    }
    public Movie getFirst() {
        lock.lock();
        try {
            return collection.getFirst();
        } finally {
            lock.unlock();
        }
    }
    public void add(Movie mv) {
        lock.lock();
        try {
            collection.add(mv);
        } finally {
            lock.unlock();
        }
    }
    public String show() {
        lock.lock();
        try {
            String elements = "";
            for (Movie mv : collection) {
                elements += mv.toString() + "----------------\n";
            }
            return elements;
        } finally {
            lock.unlock();
        }
    }
    public boolean isExistById(long id) {
        lock.lock();
        try {
            return collection.stream().anyMatch(movie -> movie.getId() == id);
        } finally {
            lock.unlock();
        }
    }
    public Movie getById(long id) {
        lock.lock();
        try {
            for (Movie mv : collection) {
                if (mv.getId() == id) {return mv;}
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
    public boolean isEmpty() {
        lock.lock();
        try {
            return collection.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    public int countByGoldenPalmCount(long goldenPalmCount) {
        lock.lock();
        try {
            return (int) collection.stream()
                    .filter(movie -> movie.getGoldenPalmCount() == goldenPalmCount)
                    .count();
        } finally {
            lock.unlock();
        }
    }
    public String printUniqueOscarsCount() {
        lock.lock();
        try {
            String[] uniqueOscarsCounts = collection.stream()
                    .filter(movie -> Objects.nonNull(movie.getOscarsCount()))
                    .map(Movie::getOscarsCount)
                    .distinct()
                    .map(Object::toString)
                    .toArray(String[]::new);

            return String.join("\n", uniqueOscarsCounts);
        } finally {
            lock.unlock();
        }
    }
    public void sort() {
        lock.lock();
        try {
            Collections.sort(collection);
        } finally {
            lock.unlock();
        }
    }
    public Boolean isIdUnique(long id) {
        lock.lock();
        try {
            for (Movie mv : collection) { if (mv.getId() == id) {return false;}}
            return true;
        } finally {
            lock.unlock();
        }
    }
    public String printFieldDescendingMpaaRating() {
        lock.lock();
        try {
            String[] ratings = collection.stream()
                    .map(Movie::getMpaaRating)
                    .sorted(Comparator.reverseOrder())
                    .map(Enum::name)
                    .toArray(String[]::new);

            return String.join("\n", ratings);
        } finally {
            lock.unlock();
        }
    }
    public void update(SqlDumper dumper) {
        lock.lock();
        try {
            List<Movie> loadedMovies = dumper.load();
            collection = new LinkedList<>();
            loadedMovies.forEach(movie -> {
                if (movie.validate()) {
                    collection.add(movie);
                } else {
                    System.out.println("Фильм с ID " + movie.getId() + " не прошел валидацию.");
                }
            });
            sort();
        } finally {
            lock.unlock();
        }
    }
    public boolean minIdOwnedBy(String username) {
        lock.lock();
        try {
            sort();
            return collection.getFirst().getCreatorName().equals(username);
        } finally {
            lock.unlock();
        }
    }
}
