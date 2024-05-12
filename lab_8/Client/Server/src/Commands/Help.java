package Commands;

import CollectionClasses.Movie;
import Managers.CollectionManager;
import Managers.Response;

public class Help extends ReferenceCommand{
    private final String responseText = "-help : вывести справку по доступным командам\n" +
            "-info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
            "-show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
            "-add {element} : добавить новый элемент в коллекцию\n" +
            "-update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
            "-remove_by_id id : удалить элемент из коллекции по его id\n" +
            "-clear : очистить коллекцию\n" +
            "-save : сохранить коллекцию в файл\n" +
            "-execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" + //
            "-exit : завершить программу (без сохранения в файл)\n" +
            "-remove_first : удалить первый элемент из коллекции\n" +
            "-head : вывести первый элемент коллекции\n" +
            "-history : вывести последние 13 команд (без их аргументов)\n" +
            "-count_by_golden_palm_count goldenPalmCount : вывести количество элементов, значение поля goldenPalmCount которых равно заданному\n" +
            "-print_unique_oscars_count : вывести уникальные значения поля oscarsCount всех элементов в коллекции\n" +
            "-print_field_descending_mpaa_rating : вывести значения поля mpaaRating всех элементов в порядке убывания";
    public Help (String name, CollectionManager collectionManager) {
        super(name, collectionManager);
    }

    @Override
    public Response execute(Movie obj, String argument, String username) {
        if (obj != null || argument != null) {return new Response("help-command doesn't allow arguments");}
        else {return new Response(responseText);}
    }
}
