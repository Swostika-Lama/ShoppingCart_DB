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

    // Dynamic items container (each row: quantity + price)
    @FXML private VBox itemsContainer;

    // Total
    @FXML private Button calculateButton;
    @FXML private Label totalLabel;

    @FXML private AnchorPane rootPane;

    private LocalizationService localizationService;
    private Locale currentLocale = new Locale("en", "US");

    // Keep track of the dynamic rows so we can read values and re-localize when language changes
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

    @FXML
    public void initialize() {
        localizationService = new LocalizationService();

        dropDown.getItems().addAll("English", "Finnish", "Swedish", "Japanese", "Arabic");
        dropDown.setValue("English");

        // Debug: print DB-sourced labels for the current locale to help diagnose missing labels
        try {
            System.out.println("[Localization] select.language -> " + localizationService.get("select.language", currentLocale));
            System.out.println("[Localization] enter.number.of.items -> " + localizationService.get("enter.number.of.items", currentLocale));
            System.out.println("[Localization] calculate.total -> " + localizationService.get("calculate.total", currentLocale));
        } catch (Exception e) {
            System.err.println("[Localization] Error while fetching initial translations: " + e.getMessage());
        }

        // Initially, no dynamic rows
        calculateButton.setDisable(true);

        updateTexts();
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
                rootPane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                break;
            default:
                currentLocale = new Locale("en", "US");
                rootPane.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        updateTexts();
    }

    private void updateTexts() {
        // Fetch translations but only overwrite the UI if a translation exists.
        String t;

        t = localizationService.get("select.language", currentLocale);
        if (t != null) selectLabel.setText(t);

        t = localizationService.get("confirm.language", currentLocale);
        if (t != null) languageConfirm.setText(t);

        t = localizationService.get("enter.number.of.items", currentLocale);
        if (t != null) itemLabel.setText(t);

        t = localizationService.get("number.of.items.prompt", currentLocale);
        if (t != null) itemPlaceholder.setPromptText(t);

        t = localizationService.get("enter.items", currentLocale);
        if (t != null) itemButton.setText(t);

        t = localizationService.get("calculate.total", currentLocale);
        if (t != null) calculateButton.setText(t);

        t = localizationService.get("total.cost", currentLocale);
        if (t != null) totalLabel.setText(t);

        // Update dynamic rows labels & prompts
        for (int i = 0; i < itemRows.size(); i++) {
            ItemRow row = itemRows.get(i);
            String qtyLabelText = localizationService.get("enter.quantity", currentLocale);
            if (qtyLabelText != null) row.quantityLabel.setText(qtyLabelText + " " + (i+1));

            String qtyPrompt = localizationService.get("quantity.prompt", currentLocale);
            if (qtyPrompt != null) row.quantityField.setPromptText(qtyPrompt);

            String priceLabelText = localizationService.get("enter.price", currentLocale);
            if (priceLabelText != null) row.priceLabel.setText(priceLabelText);

            String pricePrompt = localizationService.get("price.prompt", currentLocale);
            if (pricePrompt != null) row.priceField.setPromptText(pricePrompt);
        }
    }


    @FXML
    public void handleItem() {
        try {
            int itemCount = Integer.parseInt(itemPlaceholder.getText());
            if (itemCount <= 0) {
                String err = localizationService.get("error.invalid.item.count", currentLocale);
                if (err == null) err = localizationService.get("error.invalid.number", currentLocale);
                if (err == null) err = "Please enter a valid positive number of items.";
                showError(err);
                return;
            }

            // Clear existing rows and create itemCount rows
            itemsContainer.getChildren().clear();
            itemRows.clear();

            for (int i = 0; i < itemCount; i++) {
                Label qLabel = new Label();
                String qtyLabelText = localizationService.get("enter.quantity", currentLocale);
                if (qtyLabelText != null) qLabel.setText(qtyLabelText + " " + (i+1));
                else qLabel.setText("Enter Quantity : " + " " + (i+1));

                TextField qField = new TextField();
                String qtyPrompt = localizationService.get("quantity.prompt", currentLocale);
                if (qtyPrompt != null) qField.setPromptText(qtyPrompt);
                else qField.setPromptText("Enter number of quantity");

                Label pLabel = new Label();
                String priceLabelText = localizationService.get("enter.price", currentLocale);
                if (priceLabelText != null) pLabel.setText(priceLabelText);
                else pLabel.setText("Enter Price :");

                TextField pField = new TextField();
                String pricePrompt = localizationService.get("price.prompt", currentLocale);
                if (pricePrompt != null) pField.setPromptText(pricePrompt);
                else pField.setPromptText("Enter price");

                HBox row = new HBox(8);
                row.setPadding(new Insets(4));
                row.getChildren().addAll(qLabel, qField, pLabel, pField);
                HBox.setMargin(qLabel, new Insets(0, 8, 0, 0));
                HBox.setMargin(pLabel, new Insets(0, 8, 0, 8));

                itemsContainer.getChildren().add(row);
                itemRows.add(new ItemRow(qLabel, qField, pLabel, pField));
            }

            // Enable calculate once rows exist
            calculateButton.setDisable(false);

        } catch (NumberFormatException e) {
            String err = localizationService.get("error.invalid.input", currentLocale);
            if (err == null) err = "Please enter valid numeric values for price and quantity.";
            showError(err);
        }
    }

    @FXML
    public void handleCalculate() {
        try {
            List<CartItem> items = new ArrayList<>();
            double grandTotal = 0.0;

            for (int i = 0; i < itemRows.size(); i++) {
                ItemRow row = itemRows.get(i);
                String priceText = row.priceField.getText();
                String qtyText = row.quantityField.getText();

                double price = Double.parseDouble(priceText);
                int quantity = Integer.parseInt(qtyText);

                double subtotal = ShoppingCartCalculator.calculateItemTotal(price, quantity);
                grandTotal += subtotal;

                CartItem item = new CartItem();
                item.setItemNumber(i + 1);
                item.setPrice(price);
                item.setQuantity(quantity);
                item.setSubtotal(subtotal);
                items.add(item);
            }

            String totalText = localizationService.get("total.cost", currentLocale);
            if (totalText == null) totalText = "Total Cost is :";
            totalLabel.setText(totalText + " " + grandTotal);

            // SAVE TO DATABASE
            CartService cartService = new CartService();
            cartService.saveCart(items, currentLocale.getLanguage());

        } catch (NumberFormatException e) {
            String err = localizationService.get("error.invalid.input", currentLocale);
            if (err == null) err = "Please enter valid numeric values for price and quantity.";
            showError(err);
        }
    }
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String title = localizationService.get("error.title", currentLocale);
        if (title == null) title = "Error";
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
