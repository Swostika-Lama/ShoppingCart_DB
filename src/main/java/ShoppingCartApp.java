import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ShoppingCartApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("FXML/shopping.fxml")
        );

        Scene scene = new Scene(loader.load());

        // Try to get title from DB via LocalizationService (no resource bundle)
        String title = "Shopping Cart";
        try {
            service.LocalizationService ls = new service.LocalizationService();
            String t = ls.get("app.title", java.util.Locale.getDefault());
            if (t != null) title = t;
        } catch (Exception ignored) {
        }

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

}