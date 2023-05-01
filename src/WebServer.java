import com.hp.gagawa.java.elements.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class WebServer {

    public static void main(String[] args) throws IOException {
        InetSocketAddress servSocket = new InetSocketAddress(8500);
        HttpServer server = HttpServer.create(servSocket, 0);
        HttpContext root = server.createContext("/");
        root.setHandler(WebServer::handleRequest);

        HttpContext user = server.createContext("/users");
        user.setHandler(WebServer::handleRequestUser);

        HttpContext allUsers = server.createContext("/users/all");
        allUsers.setHandler(WebServer::handleRequestAllUsers);

        HttpContext product = server.createContext("/products");
        product.setHandler(WebServer::handleRequestOneProduct);

        HttpContext allProducts = server.createContext("/products/all");
        allProducts.setHandler(WebServer::handleRequestAllProducts);

        HttpContext order = server.createContext("/orders");
        order.setHandler(WebServer::handleRequestOneOrder);

        HttpContext allOrders = server.createContext("/orders/all");
        allOrders.setHandler(WebServer::handleRequestAllOrders);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Online shopping web server") );
        head.appendChild( title );

        Body body = new Body();

        P para = new P();

        A link = new A("/products/all", "/products/all");
        link.appendText("Product list");
        para.appendChild(link);
        body.appendChild(para);

        para = new P();
        link = new A("/orders/all", "/orders/all");
        link.appendText("Orders list");
        para.appendChild(link);
        body.appendChild(para);

        para = new P();
        link = new A("/users/all", "/users/all");
        link.appendText("Users list");
        para.appendChild(link);
        body.appendChild(para);


        html.appendChild( body );
        String response = html.write();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestUser(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));
        System.out.println(id);


        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);
        UserModel user = sqLiteDataAdapter.loadUser(id);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("User #" + user.userID));
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("User #" + user.userID));
        body.appendChild( h1 );

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);


        if (user != null) {

            para = new P();
            para.appendText("UserID: " + user.userID);
            html.appendChild(para);
            para = new P();
            para.appendText("Username: " + user.username);
            html.appendChild(para);
            para = new P();
            para.appendText("Password: " + user.password);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("User not found");
            html.appendChild(para);
        }

        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllUsers(HttpExchange exchange) throws IOException {
//        String response =  "This simple web server is designed with help from ChatGPT!";

        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);

        List<UserModel> list = sqLiteDataAdapter.loadAllUsers();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("All Users Info") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("All Users Info") );
        body.appendChild( h1 );

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " users." ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("UserID"); row.appendChild(header);
        header = new Th(); header.appendText("Username"); row.appendChild(header);
        header = new Th(); header.appendText("Password"); row.appendChild(header);
        table.appendChild(row);

        for (UserModel user : list) {
            row = new Tr();
            A link = new A("/users/" + user.userID, "");
            link.appendText(String.valueOf(user.userID));
            Td cell = new Td(); cell.appendChild(link); row.appendChild(cell);
            cell = new Td(); cell.appendText(user.username); row.appendChild(cell);
            cell = new Td(); cell.appendText(user.password); row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);
        String response = html.write();

        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestAllProducts(HttpExchange exchange) throws IOException {
//        String response =  "This simple web server is designed with help from ChatGPT!";

        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);

        List<ProductModel> list = sqLiteDataAdapter.loadAllProducts();

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("All Products Info") );
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("All Products Info") );
        body.appendChild( h1 );

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);

        para = new P();
        para.appendChild( new Text("The server has " + list.size() + " products." ));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th(); header.appendText("ProductID"); row.appendChild(header);
        header = new Th(); header.appendText("Product name"); row.appendChild(header);
        header = new Th(); header.appendText("Seller name"); row.appendChild(header);
        header = new Th(); header.appendText("Price"); row.appendChild(header);
        header = new Th(); header.appendText("Quantity"); row.appendChild(header);
        table.appendChild(row);

        for (ProductModel product : list) {
            row = new Tr();
            A link = new A("/products/" + product.productID, "");
            link.appendText(String.valueOf(product.productID));
            Td cell = new Td(); cell.appendChild(link); row.appendChild(cell);
            cell = new Td(); cell.appendText(product.productName); row.appendChild(cell);
            cell = new Td(); cell.appendText(product.sellerName); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.price)); row.appendChild(cell);
            cell = new Td(); cell.appendText(String.valueOf(product.quantity)); row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);
        String response = html.write();

        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    private static void handleRequestOneProduct(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));
        System.out.println(id);


        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);
        ProductModel product = sqLiteDataAdapter.loadProduct(id);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Product #" + product.productID));
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Product #" + product.productID));
        body.appendChild( h1 );

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);


        if (product != null) {

            para = new P();
            para.appendText("ProductID: " + product.productID);
            html.appendChild(para);
            para = new P();
            para.appendText("Product name: " + product.productName);
            html.appendChild(para);
            para = new P();
            para.appendText("Seller name: " + product.sellerName);
            html.appendChild(para);
            para = new P();
            para.appendText("Price: " + product.price);
            html.appendChild(para);
            para = new P();
            para.appendText("Quantity: " + product.quantity);
            html.appendChild(para);
        }
        else {
            para = new P();
            para.appendText("Product not found");
            html.appendChild(para);
        }

        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAllOrders(HttpExchange exchange) throws IOException {
//        String response =  "This simple web server is designed with help from ChatGPT!";

        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);

        List<OrderModel> list = sqLiteDataAdapter.loadAllOrders();

        Html html = new Html();
        Head head = new Head();

        html.appendChild(head);

        Title title = new Title();
        title.appendChild(new Text("All Orders Info"));
        head.appendChild(title);

        Body body = new Body();

        html.appendChild(body);

        H1 h1 = new H1();
        h1.appendChild(new Text("All Orders Info"));
        body.appendChild(h1);

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);

//        para = new P();
        para.appendChild(new Text("The server has " + list.size() + " orders."));
        body.appendChild(para);

        Table table = new Table();
        Tr row = new Tr();
        Th header = new Th();
        header.appendText("OrderID");
        row.appendChild(header);
        header = new Th();
        header.appendText("Product name");
        row.appendChild(header);
        header = new Th();
        header.appendText("Buyer name");
        row.appendChild(header);
        header = new Th();
        header.appendText("Seller name");
        row.appendChild(header);
        header = new Th();
        header.appendText("Price");
        row.appendChild(header);
        header = new Th();
        header.appendText("Quantity");
        row.appendChild(header);
        header = new Th();
        header.appendText("Shipping Address");
        row.appendChild(header);
        table.appendChild(row);

        for (OrderModel order : list) {
            row = new Tr();
            A link = new A("/orders/" + order.orderID, "");
            link.appendText(String.valueOf(order.orderID));
            Td cell = new Td();
            cell.appendChild(link);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(order.productName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(order.buyerName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(order.sellerName);
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(String.valueOf(order.price));
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(String.valueOf(order.quantity));
            row.appendChild(cell);
            cell = new Td();
            cell.appendText(order.shippingDestinationAddress);
            row.appendChild(cell);
            table.appendChild(row);
        }

        table.setBorder("1");

        html.appendChild(table);
        String response = html.write();

        System.out.println(response);


        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static void handleRequestOneOrder(HttpExchange exchange) throws IOException {
        String uri =  exchange.getRequestURI().toString();

        int id = Integer.parseInt(uri.substring(uri.lastIndexOf('/')+1));

        System.out.println(id);


        String url = "jdbc:sqlite:shop.db";

        SQLiteDataAdapter sqLiteDataAdapter = new SQLiteDataAdapter();

        sqLiteDataAdapter.connect(url);
        OrderModel order = sqLiteDataAdapter.loadOrder(id);

        Html html = new Html();
        Head head = new Head();

        html.appendChild( head );

        Title title = new Title();
        title.appendChild( new Text("Order #" + order.orderID));
        head.appendChild( title );

        Body body = new Body();

        html.appendChild( body );

        H1 h1 = new H1();
        h1.appendChild( new Text("Order #" + order.orderID));
        body.appendChild( h1 );

        P para = new P();
//        para.appendChild( new Text("The server time is " + LocalDateTime.now()) );
//        body.appendChild(para);


        if (order != null) {
            para = new P();
            para.appendText("OrderID: " + order.orderID);
            html.appendChild(para);
            para = new P();
            para.appendText("Product name: " + order.productName);
            html.appendChild(para);
            para = new P();
            para.appendText("Buyer name: " + order.buyerName);
            html.appendChild(para);
            para = new P();
            para.appendText("Seller name: " + order.sellerName);
            html.appendChild(para);
            para = new P();
            para.appendText("Price: " + order.price);
            html.appendChild(para);
            para = new P();
            para.appendText("Quantity: " + order.quantity);
            html.appendChild(para);
            para = new P();
            para.appendText("Shipping Address: " + order.shippingDestinationAddress);
        } else {
            para = new P();
            para.appendText("Order not found");
        }

        html.appendChild(para);


        String response = html.write();

        System.out.println(response);

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}



