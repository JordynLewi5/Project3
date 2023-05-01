import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDataAdapter implements DataAccess {
    Connection conn = null;

    @Override
    public void connect(String url) {
        try {
            // db parameters
            // create a connection to the database
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(url);

            if (conn == null)
                System.out.println("Cannot make the connection!!!");
            else
                System.out.println("The connection object is " + conn);

            System.out.println("Connection to SQLite has been established.");

            /* Test data!!!
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            while (rs.next())
                System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
            */

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void saveProduct(ProductModel product) {
        try {
            Statement stmt = conn.createStatement();

            if (loadProduct(product.productID) == null) {           // this is a new product!
                stmt.execute("INSERT INTO Products(productID, productName, sellerName, price, quantity) VALUES ("
                        + product.productID + ","
                        + '\'' + product.productName + '\'' + ","
                        + "sellerName = " + '\'' + product.sellerName + '\'' + ","
                        + product.price + ","
                        + product.quantity + ")"
                );
            }
            else {
                stmt.executeUpdate("UPDATE Products SET "
                        + "productID = " + product.productID + ","
                        + "productName = " + '\'' + product.productName + '\'' + ","
                        + "sellerName = " + '\'' + product.sellerName + '\'' + ","
                        + "price = " + product.price + ","
                        + "quantity = " + product.quantity +
                        " WHERE productID = " + product.productID
                );

            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void updateProductPrice(int id, double price) {
        try {
            Statement stmt = conn.createStatement();
                stmt.executeUpdate("UPDATE Products SET"
                        + " price = " + price
                        + " WHERE productID = " + id
                );
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void updateProductQuantity(int id, int quantity) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE Products SET"
                    + " quantity = " + quantity
                    + " WHERE productID = " + id
            );
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public ProductModel loadProduct(int productID) {
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products WHERE productID = " + productID);
            if (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.productName = rs.getString(2);
                product.sellerName = rs.getString(3);
                product.price = rs.getDouble(4);
                product.quantity = rs.getInt(5);
            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public List<ProductModel> loadAllProducts() {
        List<ProductModel> list = new ArrayList<>();
        ProductModel product = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Products ");
            while (rs.next()) {
                product = new ProductModel();
                product.productID = rs.getInt(1);
                product.productName = rs.getString(2);
                product.sellerName = rs.getString(3);
                product.price = rs.getDouble(4);
                product.quantity = rs.getDouble(5);
                list.add(product);
            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    @Override
    public OrderModel loadOrder(int orderID) {
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders WHERE orderID = " + orderID);
            if (rs.next()) {
                order = new OrderModel();
                order.orderID = rs.getInt(1);
                order.productName = rs.getString(2);
                order.buyerName = rs.getString(3);
                order.sellerName = rs.getString(4);
                order.price = rs.getDouble(5);
                order.quantity = rs.getInt(6);
                order.shippingDestinationAddress = rs.getString(7);

            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }
    @Override
    public List<OrderModel> loadAllOrders() {
        List<OrderModel> list = new ArrayList<>();
        OrderModel order = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Orders ");
            while (rs.next()) {
                order = new OrderModel();
                order.orderID = rs.getInt(1);
                order.productName = rs.getString(2);
                order.buyerName = rs.getString(3);
                order.sellerName = rs.getString(4);
                order.price = rs.getDouble(5);
                order.quantity = rs.getInt(6);
                order.shippingDestinationAddress = rs.getString(7);
                list.add(order);
            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    @Override
    public List<UserModel> loadAllUsers() {
        List<UserModel> list = new ArrayList<>();
        UserModel user = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users ");
            while (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt(1);
                user.username = rs.getString(2);
                user.password = rs.getString(3);
                list.add(user);
            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public UserModel loadUser(int userID) {
        UserModel user = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE userID = " + userID);
            if (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt(1);
                user.username = rs.getString(2);
                user.password = rs.getString(3);
            }
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

    @Override
    public UserModel loadUser(String username) {
        UserModel user = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE username = '" + username + "'");
            if (rs.next()) {
                user = new UserModel();
                user.userID = rs.getInt(1);
                user.username = rs.getString(2);
                user.password = rs.getString(3);
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return user;
    }

}
