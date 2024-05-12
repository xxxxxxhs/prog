package Interface;

import CollectionClasses.Movie;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class MovieTableModel extends AbstractTableModel {
    private List<Movie> movies;
    private String[] columnNames;
    private ResourceBundle messages;

    public MovieTableModel(ResourceBundle messages) {
        this.movies = new LinkedList<>(); // Инициализируем пустой список
        this.messages = messages;
        loadColumnNames();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        fireTableDataChanged(); // Оповещаем, что данные изменились
    }

    public void filterTable(int index) {
        Comparator<Movie> comparator = null;
        switch (index) {
            case 0:
                comparator = Comparator.comparing(Movie::getId);
                break;
            case 1:
                comparator = Comparator.comparing(Movie::getName);
                break;
            case 2:
                comparator = Comparator.comparing(movie -> movie.getCoordinates().getX());
                break;
            case 3:
                comparator = Comparator.comparing(movie -> movie.getCoordinates().getY());
                break;
            case 4:
                comparator = Comparator.comparing(Movie::getOscarsCount, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 5:
                comparator = Comparator.comparing(Movie::getGoldenPalmCount);
                break;
            case 6:
                comparator = Comparator.comparing(Movie::getTotalBoxOffice, Comparator.nullsFirst(Comparator.naturalOrder()));
                break;
            case 7:
                comparator = Comparator.comparing(Movie::getCreationDate);
                break;
            case 8:
                comparator = Comparator.comparing(Movie::getMpaaRating);
                break;
            case 9:
                comparator = Comparator.comparing(movie -> movie.getScreenwriter().getName());
                break;
            case 10:
                comparator = Comparator.comparing(movie -> movie.getScreenwriter().getLocation().getName());
                break;
            default:
                throw new IllegalArgumentException("Unknown filter index: " + index);
        }

        if (comparator != null) {
            movies = movies.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
            fireTableDataChanged();  // Обновляем таблицу после сортировки
        }
    }

    public void updateLanguage(ResourceBundle messages) {
        this.messages = messages;
        loadColumnNames();
        fireTableStructureChanged();
    }

    private void loadColumnNames() {
        columnNames = new String[] {
                messages.getString("name"),
                messages.getString("id"),
                messages.getString("x-cor"),
                messages.getString("y-cor"),
                messages.getString("oscars_count"),
                messages.getString("golden_palm_count"),
                messages.getString("total_box_office"),
                messages.getString("creation_date"),
                messages.getString("mpaa_rating"),
                messages.getString("screenwriter"),
                messages.getString("location_name"),
                messages.getString("creator_name")
        };
    }

    @Override
    public int getRowCount() {
        return movies.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Movie movie = movies.get(rowIndex);
        switch (columnIndex) {
            case 0: return movie.getName();
            case 1: return movie.getId();
            case 2: return movie.getCoordinates().getX();
            case 3: return movie.getCoordinates().getY();
            case 4: return movie.getOscarsCount();
            case 5: return movie.getGoldenPalmCount();
            case 6: return movie.getTotalBoxOffice();
            case 7: return movie.getCreationDate();
            case 8: return movie.getMpaaRating();
            case 9: return movie.getScreenwriter().getName();
            case 10: return movie.getScreenwriter().getLocation().getName();
            case 11: return movie.getCreatorName();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

