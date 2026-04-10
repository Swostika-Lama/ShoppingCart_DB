package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.LocalizationService;
import db.DBConnection;

import java.sql.Connection;
import java.util.Locale;

public class ShoppingCartApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 1. Create DB connection once
        Connection conn = DBConnection.getConnection();

        // 2. Create localization service
        LocalizationService ls = new LocalizationService(conn);

        // 3. Load FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/FXML/shopping.fxml")
        );

        Scene scene = new Scene(loader.load());

        // 4. Set translated title
        Locale locale = Locale.getDefault();
        String title = ls.get("app.title", locale);
        stage.setTitle(title);

        // 5. Inject LocalizationService into controller
        controller.ShoppingCartController controller = loader.getController();
        controller.setLocalizationService(ls);

        stage.setScene(scene);
        stage.show();
    }

}
