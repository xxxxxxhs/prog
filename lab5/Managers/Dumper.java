package Managers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private final Console console;
    private final String fileName;

    public Dumper(String fileName, Console console){
        this.fileName = fileName;
        this.console = console;
    }
    public void save (LinkedList<Movie> films) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(gson.toJson(films));
        } catch (NoSuchFileException e) {console.printError("no such file");} 
        catch (IOException e) {e.printStackTrace();}
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
            e.printStackTrace();
            System.exit(1);
        }
        console.printError("collection hasn't been upload");
        return null;
    }
}
