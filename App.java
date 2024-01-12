import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {

    ProjectTesterImp model;
    View view;

    public void start(Stage primaryStage){
        model = new ProjectTesterImp();
        view = new View(model);
        model.initialize();
        model.crawl("https://people.scs.carleton.ca/~davidmckenney/fruits/N-0.html"); //CHANGE THE CRAWL SEED HERE

        primaryStage.setTitle("Koogle Search -_-");
        primaryStage.setScene(new Scene(view, 800, 800));
        primaryStage.show();

        view.getSearchButton().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                handleSearch();
            }
        });

        view.getQueryField().setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e){view.update(); }
        });

        view.update();
    }

    public void handleSearch() {
        String query = view.getQueryField().getText();
        Boolean boost = view.getBoostCheck().isSelected();
         if (query.length() > 0) {
            model.search(query, boost, 10);
            view.getQueryField().setText("");
            view.getBoostCheck().setSelected(false);
         }
         view.update();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
