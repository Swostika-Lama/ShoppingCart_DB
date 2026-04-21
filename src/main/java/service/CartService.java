package service;

import dao.CartDAO;
import dao.CartItemDAO;
import model.CartItem;
import model.CartRecord;

import java.util.List;

public class CartService {

    private CartDAO cartDAO = new CartDAO();
    private CartItemDAO itemDAO = new CartItemDAO();

    // FIXED: use int languageId
    public void saveCart(List<CartItem> items, int languageId) {

        int totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        double totalCost = items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();

        CartRecord cart = new CartRecord();
        cart.setTotalItems(totalItems);
        cart.setTotalCost(totalCost);

        // 🔥 FIX: store languageId instead of String
        cart.setLanguageId(languageId);

        int cartId = cartDAO.insertCart(cart);

        for (CartItem item : items) {
            item.setCartRecordId(cartId);
            itemDAO.insertItem(item);
        }
    }
}