package service;

import dao.CartDAO;
import dao.CartItemDAO;
import model.CartItem;
import model.CartRecord;

import java.util.List;

public class CartService {

    private CartDAO cartDAO = new CartDAO();
    private CartItemDAO itemDAO = new CartItemDAO();

    public void saveCart(List<CartItem> items, String language) {

        int totalItems = items.stream().mapToInt(CartItem::getQuantity).sum();
        double totalCost = items.stream().mapToDouble(CartItem::getSubtotal).sum();

        CartRecord cart = new CartRecord();
        cart.setTotalItems(totalItems);
        cart.setTotalCost(totalCost);
        cart.setLanguage(language);

        int cartId = cartDAO.insertCart(cart);

        for (CartItem item : items) {
            item.setCartRecordId(cartId);
            itemDAO.insertItem(item);
        }
    }
}