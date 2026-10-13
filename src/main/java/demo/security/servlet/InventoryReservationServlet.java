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
    String quantityParameter = request.getParameter("quantity");
    int quantity;

    try {
      quantity = Integer.parseInt(quantityParameter);
    } catch (NumberFormatException e) {
      sendBadRequest(response);
      return;
    }

    if (quantity <= 0) {
      sendBadRequest(response);
      return;
    }

    if (reserveInventory(sku, quantity)) {
      response.getWriter().println("Order accepted");
    } else {
      response.sendError(HttpServletResponse.SC_CONFLICT, "Item out of stock");
    }
  }

  private void sendBadRequest(HttpServletResponse response) {
    try {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantity must be a positive integer");
    } catch (IOException e) {
      // Unable to send the error response.
    }
  }


  private boolean reserveInventory(String sku, int quantity) {
    if (quantity <= 0) {
      return false;
    }

    int available = inventory.getOrDefault(sku, 0);

    if (available < quantity) {
      return false;
    }

    // Unsafe check-then-act sequence: concurrent requests can both pass the
    // availability check and each reserve the same final item.
    int remaining = available - quantity;
    inventory.put(sku, remaining);
    return true;
  }
}
