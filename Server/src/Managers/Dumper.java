package Managers;

import java.io.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.lang.reflect.Type;
import java.nio.file.NoSuchFileException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import CollectionClasses.Movie;

/*
 * class Dumper works with json-file, saves and loads collection
 */

public class Dumper {
    
    final Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(LocalDateTime.class, new DateSerializer())
        .create();
    private final Managers.Console console;
    private final String fileName;

    public Dumper(String fileName, Managers.Console console){
        this.fileName = fileName;
        this.console = console;
        boolean isCanWrite = new File(fileName).canWrite();
        if (!isCanWrite) console.printError("File can't be written");
    }
    public void save (LinkedList<Movie> films) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(gson.toJson(films));
        } catch (NoSuchFileException e) {console.printError("no such file");} 
        catch (IOException e) {console.printError("collection can't be saved");}
    }
    public LinkedList<Movie> load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            Type filmsType = new TypeToken<LinkedList<Movie>>(){}.getType();
            String line;
            String jsonLine = "";
            while ((line = reader.readLine()) != null) {
                jsonLine += line;
            }
            LinkedList<Movie> films = gson.fromJson(jsonLine, filmsType);
            console.println("collection's been upload");
            reader.close();
            return films;
        } catch (FileNotFoundException e) {
            console.printError("file not found");
            System.exit(1);
        } catch (IOException e) {
            console.printError("something went wrong");
            System.exit(1);
        } catch (Exception e) {
            console.printError("something went wrong");
            System.exit(1);
        }
        console.printError("collection hasn't been upload");
        return null;
    }
}
