import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class View extends Pane {
    private ProjectTesterImp model;
    private ListView<SearchResult> results;
    private Button searchButton;
    private TextField queryField;
    private CheckBox boostCheck;

    public ListView<SearchResult> getResults() {
        return results;
    }

    public TextField getQueryField() {
        return queryField;
    }

    public CheckBox getBoostCheck() {
        return  boostCheck;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public View(ProjectTesterImp model) {
        this.model = model;

        //Labels
        Label titleLabel = new Label("KOOGLE-_-");
        titleLabel.setFont(new Font("Comic Sans MS", 69));
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.relocate(215, 50);
        titleLabel.setPrefSize(550, 125);
        Label queryLabel = new Label("Query");
        queryLabel.relocate(100, 225);
        Label boostLabel = new Label("Boost");
        boostLabel.relocate(535, 350);
        Label resultLabel = new Label("Results:");
        resultLabel.relocate(50, 375);

        //Text Field
        queryField = new TextField();
        queryField.relocate(100, 250);
        queryField.setPrefSize(600, 50);

        //List View
        results = new ListView<>();
        results.relocate(50, 400);
        results.setPrefSize(700, 350);

        //Button
        searchButton = new Button("Search!");
        searchButton.relocate(250, 325);
        searchButton.setPrefSize(200, 50);


        //CheckBox
        boostCheck = new CheckBox();
        boostCheck.relocate(500, 335);
        boostCheck.setPrefSize(50,50);

        //add to pane
        getChildren().addAll(titleLabel, queryLabel, boostLabel, resultLabel, queryField, results, searchButton, boostCheck);

    }

    public void update() {
        if (model.getResult() != null) {
            SearchResult[] x = model.getResult().toArray(new SearchResult[0]);
            results.setItems(FXCollections.observableArrayList(x));
        }
        if (queryField.getText().length() == 0) {
            searchButton.setDisable(true);
        } else {
            searchButton.setDisable(false);
        }
    }


}
