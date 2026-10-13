package demo.security.servlet;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InventoryReservationServletTest {

    @Test
    public void reserveInventory_withNegativeQuantityDoesNotChangeInventory() throws Exception {
        InventoryReservationServlet servlet = new InventoryReservationServlet();
        Method reserveInventory = InventoryReservationServlet.class.getDeclaredMethod("reserveInventory", String.class, int.class);
        reserveInventory.setAccessible(true);

        boolean reserved = (boolean) reserveInventory.invoke(servlet, "limited-edition-item", -1);

        Field inventoryField = InventoryReservationServlet.class.getDeclaredField("inventory");
        inventoryField.setAccessible(true);
        ConcurrentHashMap<String, Integer> inventory = (ConcurrentHashMap<String, Integer>) inventoryField.get(servlet);

        assertFalse(reserved);
        assertEquals(Integer.valueOf(1), inventory.get("limited-edition-item"));
    }
}
