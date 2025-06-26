package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these action
@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart cart = shoppingCartDao.getCartByUserId(userId);

            return cart;
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("products/{id}")
    public ShoppingCart addToCart(@PathVariable int id, Principal principal)
    {
        try
        {
            System.out.println(id);
            String userName = principal.getName();
            System.out.println(userName);
            User user = userDao.getByUserName(userName);
            System.out.println(user);
            int userId = user.getId();
            System.out.println(userId);
            return shoppingCartDao.create(id, userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PutMapping("products/{id}")
    public void updateCart(@PathVariable int id, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.update(id, userId);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("")
    public void deleteCart(@PathVariable int id, Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            var product = shoppingCartDao.getById(id);

            if(product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            shoppingCartDao.delete(id, userId);
        }
        catch(Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
