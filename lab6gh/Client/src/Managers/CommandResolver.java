package Managers;

import CollectionClasses.*;
import Exceptions.IncorrectValueException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CommandResolver {
    private static Set<String> commandsWithArgument = new HashSet<>();
    private static Set<String> commandsWithoutArgument = new HashSet<>();
    private static Set<String> specialCommands = new HashSet<>();
    private static HashSet<String> scriptsInProgress = new HashSet<>();

    static {
        commandsWithArgument.add("update");
        commandsWithArgument.add("remove_by_id");
        commandsWithArgument.add("count_by_golden_palm_count");
        commandsWithArgument.add("execute_script");

        // Команды, не требующие аргументов
        commandsWithoutArgument.add("add");
        commandsWithoutArgument.add("head");
        commandsWithoutArgument.add("remove_first");
        commandsWithoutArgument.add("help");
        commandsWithoutArgument.add("info");
        commandsWithoutArgument.add("show");
        commandsWithoutArgument.add("clear");
        commandsWithoutArgument.add("history");
        commandsWithoutArgument.add("print_unique_oscars_count");
        commandsWithoutArgument.add("print_field_descending_mpaa_rating");

        // Специальные команды, требующие объект Movie
        specialCommands.add("add");
        specialCommands.add("update");
    }

    /*
        private static final HashMap<String, Boolean> commands = new HashMap();

        static {
            commands.put("help", true);
            commands.put("info", true);
            commands.put("show", true);
            commands.put("add", true);
            commands.put("update", true);
            commands.put("remove_by_id", true);
            commands.put("clear", true);
            commands.put("execute_script", true);
            commands.put("exit", true);
            commands.put("remove_first", true);
            commands.put("head", true);
            commands.put("history", true);
            commands.put("count_by_golden_palm_count", true);
            commands.put("print_unique_oscars_count", true);
            commands.put("print_field_descending_mpaa_rating", true);
        }

        public Request resolve(String[] args) throws IncorrectValueException {
            if (commands.getOrDefault(args[0], false)) {
                Movie movie = switch (args[0]) {
                    case "add", "update" -> Ask.newMovie(new Console());
                    default -> null;
                };
                String argument = switch (args.length){
                    case 1 -> null;
                    default -> args[1];
                };
                return new Request(args[0], movie, argument);
            }
            return null;
        }
     */
    public static LinkedList<Request> executeScript(String fileName) {
        String absolutePath = Paths.get(fileName).toAbsolutePath().toString();
        LinkedList<Request> requests = new LinkedList<>();

        if (scriptsInProgress.contains(absolutePath)) {
            System.out.println("Обнаружена рекурсия для файла: " + fileName);
            return requests;
        }

        scriptsInProgress.add(absolutePath);

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            Iterator<String> lineIterator = lines.iterator();

            while (lineIterator.hasNext()) {
                String line = lineIterator.next();
                String[] args = line.split(" ", 2);
                String command = args[0];
                String argument = args.length > 1 ? args[1] : null;

                if ("add".equals(command) || "update".equals(command)) {
                    if (lineIterator.hasNext()) {
                        Movie movie = new Movie(
                                lineIterator.next(),
                                new Coordinates(Float.parseFloat(lineIterator.next()),
                                        Double.parseDouble(lineIterator.next())),
                                Integer.parseInt(lineIterator.next()),
                                Long.parseLong(lineIterator.next()),
                                Float.parseFloat(lineIterator.next()),
                                MpaaRating.valueOf(lineIterator.next()),
                                new Person(
                                        lineIterator.next(),
                                        lineIterator.next(),
                                        Color.valueOf(lineIterator.next()),
                                        Color.valueOf(lineIterator.next()),
                                        new Location(
                                                Double.parseDouble(lineIterator.next()),
                                                Float.parseFloat(lineIterator.next()),
                                                Long.parseLong(lineIterator.next()),
                                                lineIterator.next()
                                        )
                                )
                        );
                        requests.add(new Request(command, movie, argument));
                    }
                } else if ("execute_script".equals(command)) {
                    if (!scriptsInProgress.contains(absolutePath)) {
                        requests.addAll(executeScript(argument));
                    }
                } else {
                    requests.add(new Request(command, null, argument));
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + fileName);
        } catch (IncorrectValueException e) {
            e.printStackTrace();
        } finally {
            scriptsInProgress.remove(absolutePath);
        }

        return requests;
    }
    public static Request resolve(String[] args) throws IncorrectValueException {
        if (args.length == 0 || args.length > 2) {
            System.out.println("Некорректный запрос. Ожидается 1 или 2 аргумента.");
            return null;
        }

        String command = args[0].trim();
        if (!commandsWithArgument.contains(command) && !commandsWithoutArgument.contains(command)) {
            System.out.println("Несуществующая команда: " + command);
            return null;
        }

        if (args.length == 2 && !commandsWithArgument.contains(command)) {
            System.out.println("Команда не принимает аргументы: " + command);
            return null;
        }

        if (args.length == 1 && commandsWithArgument.contains(command)) {
            System.out.println("Для команды требуется аргумент: " + command);
            return null;
        }

        String argument = args.length == 2 ? args[1].trim() : null;
        Movie movie = null;

        if (specialCommands.contains(command)) {
            movie = Ask.newMovie(new Console());
        }
        return new Request(command, movie, argument);
    }
    public static Request connect(){
        return new Request("connect", null, null);
    }
}


