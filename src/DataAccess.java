import java.util.List;

public interface DataAccess {
    void connect(String str);
    void saveProduct(ProductModel product);
    void updateProductPrice(int id, double price);
    void updateProductQuantity(int id, int quantity);
    ProductModel loadProduct(int productID);
    List<ProductModel> loadAllProducts();
    OrderModel loadOrder(int productID);
    List<OrderModel> loadAllOrders();
    UserModel loadUser(int userID);
    UserModel loadUser(String username);

    List<UserModel> loadAllUsers();

}
