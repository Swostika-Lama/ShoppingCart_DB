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

public class ShoppingCartController {

    @FXML private Label selectLabel;
    @FXML private ComboBox<String> dropDown;
    @FXML private Button languageConfirm;

    @FXML private Label itemLabel;
    @FXML private TextField itemPlaceholder;
    @FXML private Button itemButton;

    @FXML private VBox itemsContainer;

    @FXML private Button calculateButton;
    @FXML private Label totalLabel;

    @FXML private AnchorPane rootPane;

    private LocalizationService localizationService;
    private CartService cartService;

    // ✅ FIX: use language ID instead of Locale
    private int currentLanguageId = 1;

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

    public void setLocalizationService(LocalizationService ls) {
        this.localizationService = ls;
    }

    public void setCartService(CartService cs) {
        this.cartService = cs;
    }

    @FXML
    public void initialize() {
        dropDown.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        dropDown.setValue("English");
        calculateButton.setDisable(true);

        currentLanguageId = 1;
    }

    // =========================
    // LANGUAGE HANDLING FIXED
    // =========================
    @FXML
    public void handleLanguage() {
        String selected = dropDown.getValue();

        switch (selected) {
            case "Finnish":
                currentLanguageId = 2;
                break;
            case "Swedish":
                currentLanguageId = 3;
                break;
            case "Japanese":
                currentLanguageId = 4;
                break;
            case "Arabic":
                currentLanguageId = 5;
                break;
            default:
                currentLanguageId = 1;
        }

        updateTexts();
    }

    // =========================
    // UPDATE UI TEXTS
    // =========================
    private void updateTexts() {
        if (localizationService == null) return;

        // RTL support for Arabic
        rootPane.setNodeOrientation(
                currentLanguageId == 5
                        ? NodeOrientation.RIGHT_TO_LEFT
                        : NodeOrientation.LEFT_TO_RIGHT
        );

        selectLabel.setText(localizationService.get("select.language", currentLanguageId));
        languageConfirm.setText(localizationService.get("confirm.language", currentLanguageId));
        itemLabel.setText(localizationService.get("enter.number.of.items", currentLanguageId));
        itemPlaceholder.setPromptText(localizationService.get("number.of.items.prompt", currentLanguageId));
        itemButton.setText(localizationService.get("enter.items", currentLanguageId));
        calculateButton.setText(localizationService.get("calculate.total", currentLanguageId));
        totalLabel.setText(localizationService.get("total.cost", currentLanguageId));
    }

    // =========================
    // CREATE ITEM INPUT FIELDS
    // =========================
    @FXML
    public void handleItem() {
        try {
            int itemCount = Integer.parseInt(itemPlaceholder.getText());

            if (itemCount <= 0) {
                showError(localizationService.get("error.invalid.number", currentLanguageId));
                return;
            }

            itemsContainer.getChildren().clear();
            itemRows.clear();

            for (int i = 0; i < itemCount; i++) {

                Label qLabel = new Label("Qty " + (i + 1));
                TextField qField = new TextField();

                Label pLabel = new Label("Price");
                TextField pField = new TextField();

                HBox row = new HBox(8);
                row.setPadding(new Insets(4));
                row.getChildren().addAll(qLabel, qField, pLabel, pField);

                itemsContainer.getChildren().add(row);
                itemRows.add(new ItemRow(qLabel, qField, pLabel, pField));
            }

            calculateButton.setDisable(false);

        } catch (NumberFormatException e) {
            showError(localizationService.get("error.invalid.input", currentLanguageId));
        }
    }

    // =========================
    // CALCULATE TOTAL
    // =========================
    @FXML
    public void handleCalculate() {

        if (cartService == null) {
            throw new IllegalStateException("CartService not injected");
        }

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

            totalLabel.setText(
                    localizationService.get("total.cost", currentLanguageId)
                            + " " + grandTotal
            );

            cartService.saveCart(items, currentLanguageId);

        } catch (NumberFormatException e) {
            showError(localizationService.get("error.invalid.input", currentLanguageId));
        }
    }

    // =========================
    // ERROR HANDLING
    // =========================
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(localizationService.get("error.title", currentLanguageId));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}