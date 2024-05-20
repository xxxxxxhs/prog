package Managers;

import CollectionClasses.Movie;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SafeLinkedList<E> {
    private LinkedList<Movie> collection = new LinkedList<>();
    //private final ReentrantLock lock = new ReentrantLock();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public int size() {
        readLock.lock();
        try {
            return collection.size();
        } finally {
            readLock.unlock();
        }
    }
    public Movie getFirst() {
        readLock.lock();
        try {
            return collection.getFirst();
        } finally {
            readLock.unlock();
        }
    }
    public void add(Movie mv) {
        writeLock.lock();
        try {
            collection.add(mv);
        } finally {
            writeLock.unlock();
        }
    }
    public LinkedList<Movie> show() {
        readLock.lock();
        try {
            return collection;
        } finally {
            readLock.unlock();
        }
    }
    public boolean isExistById(long id) {
        readLock.lock();
        try {
            return collection.stream().anyMatch(movie -> movie.getId() == id);
        } finally {
            readLock.unlock();
        }
    }
    public Movie getById(long id) {
        readLock.lock();
        try {
            for (Movie mv : collection) {
                if (mv.getId() == id) {return mv;}
            }
            return null;
        } finally {
            readLock.unlock();
        }
    }
    public boolean isEmpty() {
        readLock.lock();
        try {
            return collection.isEmpty();
        } finally {
            readLock.unlock();
        }
    }
    public int countByGoldenPalmCount(long goldenPalmCount) {
        readLock.lock();
        try {
            return (int) collection.stream()
                    .filter(movie -> movie.getGoldenPalmCount() == goldenPalmCount)
                    .count();
        } finally {
            readLock.unlock();
        }
    }
    public String printUniqueOscarsCount() {
        readLock.lock();
        try {
            String[] uniqueOscarsCounts = collection.stream()
                    .filter(movie -> Objects.nonNull(movie.getOscarsCount()))
                    .map(Movie::getOscarsCount)
                    .distinct()
                    .map(Object::toString)
                    .toArray(String[]::new);

            return String.join("\n", uniqueOscarsCounts);
        } finally {
            readLock.unlock();
        }
    }
    public void sort() {
        writeLock.lock();
        try {
            Collections.sort(collection);
        } finally {
            writeLock.unlock();
        }
    }
    public Boolean isIdUnique(long id) {
        readLock.lock();
        try {
            for (Movie mv : collection) { if (mv.getId() == id) {return false;}}
            return true;
        } finally {
            readLock.unlock();
        }
    }
    public String printFieldDescendingMpaaRating() {
        readLock.lock();
        try {
            String[] ratings = collection.stream()
                    .map(Movie::getMpaaRating)
                    .sorted(Comparator.reverseOrder())
                    .map(Enum::name)
                    .toArray(String[]::new);

            return String.join("\n", ratings);
        } finally {
            readLock.unlock();
        }
    }
    public void update(SqlDumper dumper) {
        writeLock.lock();
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
            writeLock.unlock();
        }
    }
    public boolean minIdOwnedBy(String username) {
        readLock.lock();
        try {
            sort();
            return collection.getFirst().getCreatorName().equals(username);
        } finally {
            readLock.unlock();
        }
    }
}
