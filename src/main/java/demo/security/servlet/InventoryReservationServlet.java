package demo.security.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class InventoryReservationServlet extends HttpServlet {

  private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

  public InventoryReservationServlet() {
    inventory.put("limited-edition-item", 1);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String sku = request.getParameter("sku");
    int quantity = Integer.parseInt(request.getParameter("quantity"));

    if (reserveInventory(sku, quantity)) {
      response.getWriter().println("Order accepted");
    } else {
      response.sendError(HttpServletResponse.SC_CONFLICT, "Item out of stock");
    }
  }

  private boolean reserveInventory(String sku, int quantity) {
    int available = inventory.getOrDefault(sku, 0);

    if (available < quantity) {
      return false;
    }

    // Unsafe check-then-act sequence: concurrent requests can both pass the
    // availability check and each reserve the same final item.
    inventory.put(sku, available - quantity);
    return true;
  }
}
