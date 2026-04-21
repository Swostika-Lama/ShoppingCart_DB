package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.LocalizationService;
import service.CartService;
import db.DBConnection;

import java.sql.Connection;

public class ShoppingCartApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        if ("true".equals(System.getProperty("skip.javafx.launch"))) {
            return;
        }

        // DB connection
        Connection conn = DBConnection.getConnection();

        // Services
        LocalizationService ls = new LocalizationService(conn);
        CartService cartService = new CartService();

        // Load FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/FXML/shopping.fxml")
        );

        Scene scene = new Scene(loader.load());

        // Controller
        controller.ShoppingCartController controller = loader.getController();

        // Inject dependencies
        controller.setLocalizationService(ls);
        controller.setCartService(cartService);

        // ✅ FIX: use languageId instead of Locale
        stage.setTitle(ls.get("app.title", 1));

        stage.setScene(scene);
        stage.show();
    }
}