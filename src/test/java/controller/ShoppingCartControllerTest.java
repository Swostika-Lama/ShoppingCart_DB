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

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartControllerTest {

    private ShoppingCartController controller;
    private LocalizationService localizationService;
    private CartService cartService;

    private static void inject(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T get(Object target, String fieldName, Class<T> type) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return type.cast(f.get(target));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        new JFXPanel(); // initialize JavaFX

        controller = new ShoppingCartController();

        localizationService = mock(LocalizationService.class);
        cartService = mock(CartService.class);

        // FIXED MOCK (languageId-based)
        when(localizationService.get(anyString(), anyInt()))
                .thenAnswer(inv -> inv.getArgument(0));

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

        controller.initialize();

        controller.setLocalizationService(localizationService);
        controller.setCartService(cartService);
    }

    @Test
    void testInitialize() {
        ComboBox<String> dropDown = get(controller, "dropDown", ComboBox.class);
        Button calculateButton = get(controller, "calculateButton", Button.class);

        assertEquals("English", dropDown.getValue());
        assertTrue(calculateButton.isDisabled());
    }

    @Test
    void testHandleLanguage_finnish() {
        ComboBox<String> dropDown = get(controller, "dropDown", ComboBox.class);
        dropDown.setValue("Finnish");

        controller.handleLanguage();

        verify(localizationService, atLeastOnce())
                .get(anyString(), eq(2)); // Finnish = 2
    }

    @Test
    void testHandleLanguage_arabic_setsRTL() {
        ComboBox<String> dropDown = get(controller, "dropDown", ComboBox.class);
        dropDown.setValue("Arabic");

        controller.handleLanguage();

        AnchorPane root = get(controller, "rootPane", AnchorPane.class);

        assertEquals(
                javafx.geometry.NodeOrientation.RIGHT_TO_LEFT,
                root.getNodeOrientation()
        );
    }

    @Test
    void testHandleItem_valid() {
        TextField itemPlaceholder = get(controller, "itemPlaceholder", TextField.class);
        VBox itemsContainer = get(controller, "itemsContainer", VBox.class);
        Button calculateButton = get(controller, "calculateButton", Button.class);

        itemPlaceholder.setText("3");
        controller.handleItem();

        assertEquals(3, itemsContainer.getChildren().size());
        assertFalse(calculateButton.isDisabled());
    }

    @Test
    void testHandleCalculate_success() {
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
        verify(cartService).saveCart(captor.capture(), anyInt()); // FIXED

        List<CartItem> saved = captor.getValue();
        assertEquals(2, saved.size());
        assertEquals(20.0, saved.get(0).getSubtotal());
    }

    @Test
    void testHandleCalculate_missingCartService_throws() {
        controller.setCartService(null);
        assertThrows(IllegalStateException.class, controller::handleCalculate);
    }
}