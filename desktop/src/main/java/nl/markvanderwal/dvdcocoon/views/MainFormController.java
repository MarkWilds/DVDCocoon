package nl.markvanderwal.dvdcocoon.views;

import com.google.common.base.*;
import javafx.beans.property.*;
import javafx.collections.transformation.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import nl.markvanderwal.dvdcocoon.exceptions.*;
import nl.markvanderwal.dvdcocoon.models.*;
import nl.markvanderwal.dvdcocoon.services.*;
import org.apache.logging.log4j.*;

import javax.inject.*;
import java.net.*;
import java.util.*;

/**
 * @author Mark "Wilds" van der Wal
 * @since 1-2-2018
 */
public class MainFormController extends CocoonController {

    private static final Logger LOGGER = LogManager.getLogger(MainFormController.class);

    private final MovieService movieService;
    private final MediumService mediumService;
    private final GenreService genreService;

    private FilteredList<Movie> filteredMovies;

    @FXML
    private TableView movieTable;

    @FXML
    private TableColumn<Movie, String> labelColumn;

    @FXML
    private TableColumn<Movie, String> mediumColumn;

    @FXML
    private TableColumn<Movie, String> nameColumn;

    @FXML
    private TableColumn<Movie, String> genresColumn;

    @FXML
    private Label movieToolbar;

    @FXML
    private Button newMovieButton;

    @FXML
    private Button mediumsButton;

    @FXML
    private Button genresButton;

    /**************** FILTER *****/

    @FXML
    private CheckBox labelFilter;

    @FXML
    private CheckBox mediumFilter;

    @FXML
    private CheckBox genreFilter;

    @FXML
    private CheckBox actorFilter;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private TextField searchTextInput;

    @Inject
    public MainFormController(MovieService movieService, MediumService mediumService, GenreService genreService) {
        this.movieService = movieService;
        this.mediumService = mediumService;
        this.genreService = genreService;
    }

    @Override
    protected URL getFxmlResourceUrl() {
        return getClass().getResource("MainForm.fxml");
    }

    @Override
    protected ResourceBundle getFxmlResourceBundle() {
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeButtonIcons();
        initializeTableView();
        initializeFilterView();

        newMovieButton.setOnAction(this::showMovieForm);

        mediumsButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Media", mediumService, Medium.class);
        });

        genresButton.setOnAction(actionEvent -> {
            showValueForm(actionEvent, "Genres", genreService, Genre.class);
        });

        movieToolbar.setText(String.format("%s films geladen", movieTable.getItems().size()));

        movieTable.setRowFactory(tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Movie rowData = row.getItem();
                    showMovieForm(event, rowData);
                }
            });
            return row;
        });
    }

    private void initializeButtonIcons() {

    }

    private void initializeTableView() {
        movieTable.setEditable(false);
        movieTable.setPlaceholder(new Label("Geen films gevonden"));

        labelColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getLabel());
        });

        mediumColumn.setCellValueFactory(cellData -> {
            Medium medium = cellData.getValue().getMedium();
            String mediumText = "N.V.T";
            if (medium != null) {
                mediumText = medium.getName();
            }
            return new SimpleStringProperty(mediumText);
        });

        nameColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getName());
        });

        genresColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty("N.V.T");
        });

        filteredMovies = new FilteredList<>(movieService.bind(), this::movieFilter);
        SortedList<Movie> sortedList = new SortedList<>(filteredMovies);
        sortedList.comparatorProperty().bind(movieTable.comparatorProperty());
        movieTable.setItems(sortedList);

        try {
            movieService.fetch();
        } catch (ServiceException e) {
            LOGGER.error("Gefaald om de film data op te halen!");
        }
    }

    private void initializeFilterView() {
        clearButton.setOnAction(actionEvent -> {
            searchTextInput.setText("");
            filteredMovies.setPredicate(movie -> true);
        });

        searchButton.setOnAction(actionEvent -> {
            filteredMovies.setPredicate(this::movieFilter);
        });
    }

    private boolean movieFilter(Movie movie) {
        boolean anySelected = labelFilter.isSelected() ||
                actorFilter.isSelected() ||
                mediumFilter.isSelected();

        String searchText = searchTextInput.getText();
        if (Strings.isNullOrEmpty(searchText))
            return true;

        boolean filter = false;

        if (!anySelected) {
            filter = movie.getName().contains(searchText);
        }

        if (labelFilter.isSelected()) {
            filter = filter || movie.getLabel().contains(searchText);
        }

        if (actorFilter.isSelected()
                && !Strings.isNullOrEmpty(movie.getActors())) {
            filter = filter || movie.getActors().contains(searchText);
        }

        Medium medium = movie.getMedium();
        if (mediumFilter.isSelected()
                && medium != null && !Strings.isNullOrEmpty(medium.getName())) {
            filter = filter || medium.getName().contains(searchText);
        }

        return filter;
    }
}
