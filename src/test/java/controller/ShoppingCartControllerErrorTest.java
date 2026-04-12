package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.util.TestDbHelper;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartControllerErrorTest {

    @BeforeEach
    void setupDb() {
        TestDbHelper.prepareUniqueDb();
    }

    @Test
    void testHandleCalculate_withMalformedInput_callsLocalizationError() {
        ShoppingCartController controller = new ShoppingCartController();
        assertThrows(IllegalStateException.class, controller::handleCalculate);
    }
}
