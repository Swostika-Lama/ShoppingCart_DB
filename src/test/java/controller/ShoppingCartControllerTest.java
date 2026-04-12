package controller;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import service.CartService;
import service.LocalizationService;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingCartControllerTest {

    private ShoppingCartController controller;
    private LocalizationService localizationService;
    private CartService cartService;

    // Reflection helper to inject private @FXML fields
    private static void inject(Object controller, String fieldName, Object value) {
        try {
            var field = controller.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Reflection helper to READ private fields
    private static <T> T get(Object controller, String fieldName, Class<T> type) {
        try {
            var field = controller.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return type.cast(field.get(controller));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        new JFXPanel(); // Start JavaFX runtime

        controller = new ShoppingCartController();

        localizationService = mock(LocalizationService.class);
        cartService = mock(CartService.class);

        when(localizationService.get(anyString(), any(Locale.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Inject private UI fields
        inject(controller, "selectLabel", new Label());
        inject(controller, "dropDown", new ComboBox<>());
        inject(controller, "languageConfirm", new Button());

        inject(controller, "itemLabel", new Label());
        inject(controller, "itemPlaceholder", new TextField());
        inject(controller, "itemButton", new Button());

        inject(controller, "itemsContainer", new VBox());
        inject(controller, "calculateButton", new Button());
        inject(controller, "totalLabel", new Label());

        inject(controller, "rootPane", new AnchorPane());

        // Inject services
        controller.setLocalizationService(localizationService);
        inject(controller, "cartService", cartService);

        controller.initialize();
    }

    @Test
    void testInitialize() {
        ComboBox<String> dropDown = get(controller, "dropDown", ComboBox.class);
        Button calculateButton = get(controller, "calculateButton", Button.class);

        assertEquals("English", dropDown.getValue());
        assertTrue(calculateButton.isDisabled());
    }

    @Test
    void testHandleLanguage() {
        ComboBox<String> dropDown = get(controller, "dropDown", ComboBox.class);
        dropDown.setValue("Finnish");

        controller.handleLanguage();

        verify(localizationService, atLeastOnce())
                .get(anyString(), eq(Locale.forLanguageTag("fi-FI")));
    }

    @Test
    void testHandleItem() {
        TextField itemPlaceholder = get(controller, "itemPlaceholder", TextField.class);
        VBox itemsContainer = get(controller, "itemsContainer", VBox.class);
        Button calculateButton = get(controller, "calculateButton", Button.class);

        itemPlaceholder.setText("3");
        controller.handleItem();

        assertEquals(3, itemsContainer.getChildren().size());
        assertFalse(calculateButton.isDisabled());
    }

    @Test
    void testHandleCalculate() {
        TextField itemPlaceholder = get(controller, "itemPlaceholder", TextField.class);
        VBox itemsContainer = get(controller, "itemsContainer", VBox.class);

        itemPlaceholder.setText("2");
        controller.handleItem();

        itemsContainer.getChildren().forEach(node -> {
            HBox row = (HBox) node;
            TextField qField = (TextField) row.getChildren().get(1);
            TextField pField = (TextField) row.getChildren().get(3);

            qField.setText("2");
            pField.setText("10");
        });

        controller.handleCalculate();

        ArgumentCaptor<List<CartItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(cartService).saveCart(captor.capture(), anyString());

        List<CartItem> saved = captor.getValue();
        assertEquals(2, saved.size());
        assertEquals(20.0, saved.get(0).getSubtotal());
    }
}
