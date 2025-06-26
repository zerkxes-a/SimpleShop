package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getCartByUserId(int userId) {
        String sql = "SELECT * FROM products AS p JOIN shopping_cart as sc ON sc.product_id = p.product_id WHERE sc.user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            ShoppingCart shoppingCart = new ShoppingCart();
            while (row.next()) {
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setProduct(mapRow(row));
                shoppingCartItem.setQuantity(row.getInt("quantity"));
                shoppingCart.add(shoppingCartItem);

            }
            return shoppingCart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ShoppingCart create(int productId, int userId) {
        try (Connection connection = getConnection()) {
            String checkSql = "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES(?,?,1) ON DUPLICATE KEY UPDATE quantity = quantity + 1";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, userId);
                checkStmt.setInt(2, productId);
                checkStmt.executeUpdate();
                // Step 3: Return updated cart
                return getCartByUserId(userId);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to insert or update cart", e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error adding product to cart", e);
        }
    }

    //TODO THIS LAST, OPTIONAL IN OPTIONAL
    @Override
    public ShoppingCart update(int id, int userId) {

        return null;
    }

    //TODO STATEMENT WORKS IN SQL WHY CONNECT WEIRD IN POSTMAN AND NOT WORK ON WEBSITE
    @Override
    public ShoppingCart delete(int userId){

        try (Connection connection = getConnection()){
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();

            return getCartByUserId(userId);
            //TODO DO I NEED RETURN HERE? QUERY TOPHER CONFUESD AS TO WHY STILL NOT SHOWING UP ON WEBPAGE
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getById(int id) {
        return null;
    }

    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }


}
