package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{

    ShoppingCart getCartByUserId(int userId);

    ShoppingCart create(int productId, int userId);

    ShoppingCart update(int id, int userId);

    ShoppingCart delete(int userId);

    Object getById(int id);
    // add additional method signatures here
}
