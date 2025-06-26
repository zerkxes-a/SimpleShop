package org.yearup.data;

import org.yearup.models.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartDao
{

    ShoppingCart getCartByUserId(int userId);

    ShoppingCart create(int productId, int userId);

    void update(int id, int userId);

    void delete(int id, int userId);

    Object getById(int id);
    // add additional method signatures here
}
