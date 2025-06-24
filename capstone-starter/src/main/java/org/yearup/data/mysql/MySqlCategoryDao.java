package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{


    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories;";
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rows = statement.executeQuery();
            while(rows.next()){
                categories.add(new Category(rows.getInt(1), rows.getString(2), rows.getString(3)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        ArrayList<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories WHERE Category_id=?;";
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            ResultSet rows = statement.executeQuery();
            while(rows.next()){
                categories.add(new Category(rows.getInt(1), rows.getString(2), rows.getString(3)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
       // return categories;

        // get category by id
        return null;
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories(category_id, name, description) " +
                " VALUES (?, ?, ?);";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, category.getCategoryId());
            statement.setString(2, category.getName());
            statement.setString(3, category.getDescription());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
            // create a new category
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        @Override
    public void update(int categoryId, Category category) {

            {
                String sql = "UPDATE categories" +
                        " SET category_id = ? " +
                        "   , name = ? " +
                        "   , description = ? " +
                        " WHERE category_id = ?;";

                try (Connection connection = getConnection()) {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, categoryId);
                    statement.setString(2, category.getName());
                    statement.setString(3, category.getDescription());

                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // update category
            }
        }

    @Override
    public void delete(int categoryId) {
        String sql = "DELETE FROM categories " +
            " WHERE category_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
