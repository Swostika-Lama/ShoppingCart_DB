package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;
import model.ShoppingCartCalculator;
import service.CartService;
import service.LocalizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingCartController {

    // Language selection
    @FXML private Label selectLabel;
    @FXML private ComboBox<String> dropDown;
    @FXML private Button languageConfirm;

    // Item count
    @FXML private Label itemLabel;
    @FXML private TextField itemPlaceholder;
    @FXML private Button itemButton;

    // Dynamic items container
    @FXML private VBox itemsContainer;

    // Total
    @FXML private Button calculateButton;
    @FXML private Label totalLabel;

    @FXML private AnchorPane rootPane;

    private LocalizationService localizationService;
    private Locale currentLocale = new Locale("en", "US");

    private final List<ItemRow> itemRows = new ArrayList<>();

    private static class ItemRow {
        Label quantityLabel;
        TextField quantityField;
        Label priceLabel;
        TextField priceField;

        ItemRow(Label ql, TextField qf, Label pl, TextField pf) {
            this.quantityLabel = ql;
            this.quantityField = qf;
            this.priceLabel = pl;
            this.priceField = pf;
        }
    }

    // Injected from ShoppingCartApp
    public void setLocalizationService(LocalizationService ls) {
        this.localizationService = ls;
        updateTexts();
    }

    @FXML
    public void initialize() {
        dropDown.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        dropDown.setValue("English");

        calculateButton.setDisable(true);
    }

    @FXML
    public void handleLanguage() {
        String selected = dropDown.getValue();

        switch (selected) {
            case "Finnish": currentLocale = new Locale("fi", "FI"); break;
            case "Swedish": currentLocale = new Locale("sv", "SE"); break;
            case "Japanese": currentLocale = new Locale("ja", "JP"); break;
            case "Arabic":
                currentLocale = new Locale("ar", "AR");
                break;
            default:
                currentLocale = new Locale("en", "US");
        }

        updateTexts();
    }

    private void updateTexts() {

        // RTL support
        if (currentLocale.getLanguage().equals("ar")) {
            rootPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            rootPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        String t;

        t = localizationService.get("select.language", currentLocale);
        selectLabel.setText(t);

        t = localizationService.get("confirm.language", currentLocale);
        languageConfirm.setText(t);

        t = localizationService.get("enter.number.of.items", currentLocale);
        itemLabel.setText(t);

        t = localizationService.get("number.of.items.prompt", currentLocale);
        itemPlaceholder.setPromptText(t);

        t = localizationService.get("enter.items", currentLocale);
        itemButton.setText(t);

        t = localizationService.get("calculate.total", currentLocale);
        calculateButton.setText(t);

        t = localizationService.get("total.cost", currentLocale);
        totalLabel.setText(t);

        // Update dynamic rows
        for (int i = 0; i < itemRows.size(); i++) {
            ItemRow row = itemRows.get(i);

            row.quantityLabel.setText(localizationService.get("enter.quantity", currentLocale) + " " + (i + 1));
            row.quantityField.setPromptText(localizationService.get("quantity.prompt", currentLocale));

            row.priceLabel.setText(localizationService.get("enter.price", currentLocale));
            row.priceField.setPromptText(localizationService.get("price.prompt", currentLocale));
        }
    }

    @FXML
    public void handleItem() {
        try {
            int itemCount = Integer.parseInt(itemPlaceholder.getText());
            if (itemCount <= 0) {
                showError(localizationService.get("error.invalid.number", currentLocale));
                return;
            }

            itemsContainer.getChildren().clear();
            itemRows.clear();

            for (int i = 0; i < itemCount; i++) {

                Label qLabel = new Label(localizationService.get("enter.quantity", currentLocale) + " " + (i + 1));
                TextField qField = new TextField();
                qField.setPromptText(localizationService.get("quantity.prompt", currentLocale));

                Label pLabel = new Label(localizationService.get("enter.price", currentLocale));
                TextField pField = new TextField();
                pField.setPromptText(localizationService.get("price.prompt", currentLocale));

                HBox row = new HBox(8);
                row.setPadding(new Insets(4));
                row.getChildren().addAll(qLabel, qField, pLabel, pField);

                itemsContainer.getChildren().add(row);
                itemRows.add(new ItemRow(qLabel, qField, pLabel, pField));
            }

            calculateButton.setDisable(false);

        } catch (NumberFormatException e) {
            showError(localizationService.get("error.invalid.input", currentLocale));
        }
    }

    @FXML
    public void handleCalculate() {
        try {
            List<CartItem> items = new ArrayList<>();
            double grandTotal = 0.0;

            for (int i = 0; i < itemRows.size(); i++) {
                ItemRow row = itemRows.get(i);

                double price = Double.parseDouble(row.priceField.getText());
                int quantity = Integer.parseInt(row.quantityField.getText());

                double subtotal = ShoppingCartCalculator.calculateItemTotal(price, quantity);
                grandTotal += subtotal;

                CartItem item = new CartItem();
                item.setItemNumber(i + 1);
                item.setPrice(price);
                item.setQuantity(quantity);
                item.setSubtotal(subtotal);
                items.add(item);
            }

            totalLabel.setText(localizationService.get("total.cost", currentLocale) + " " + grandTotal);

            CartService cartService = new CartService();
            cartService.saveCart(items, currentLocale.getLanguage());

        } catch (NumberFormatException e) {
            showError(localizationService.get("error.invalid.input", currentLocale));
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(localizationService.get("error.title", currentLocale));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
