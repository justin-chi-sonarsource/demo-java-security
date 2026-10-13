package demo.security.servlet;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class InventoryReservationServlet extends HttpServlet {

  private final ConcurrentHashMap<String, Integer> inventory = new ConcurrentHashMap<>();

  public InventoryReservationServlet() {
    inventory.put("limited-edition-item", 1);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String sku = request.getParameter("sku");
    int quantity;

    try {
      quantity = Integer.parseInt(request.getParameter("quantity"));
    } catch (NumberFormatException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity");
      return;
    }

    if (quantity <= 0) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity");
      return;
    }

    if (reserveInventory(sku, quantity)) {
      response.getWriter().println("Order accepted");
    } else {
      response.sendError(HttpServletResponse.SC_CONFLICT, "Item out of stock");
    }
  }

  private boolean reserveInventory(String sku, int quantity) {
    if (quantity <= 0) {
      return false;
    }

    boolean[] reserved = {false};
    inventory.compute(sku, (key, available) -> {
      if (available == null || available < quantity) {
        return available;
      }

      int newAvailable = available - quantity;
      if (newAvailable < 0 || newAvailable > available) {
        return available;
      }

      reserved[0] = true;
      return newAvailable;
    });
    return reserved[0];
  }
}
