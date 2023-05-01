import com.google.gson.Gson;
import com.hp.gagawa.java.elements.Q;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TestClient {
    public static void main(String[] args) throws IOException {
        for (;;) {
            // ask for service from Registry
            Socket socket = new Socket("localhost", 5000);

            Scanner scanner = new Scanner(System.in);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.print("\nPlease select a service number (1-7):"
                    + "\nPRODUCT_INFO_SERVICE = 1"
                    + "\nORDER_INFO_SERVICE = 2"
                    + "\nUSER_INFO_SERVICE = 3"
                    + "\nPRODUCT_PRICE_UPDATE_SERVICE = 4"
                    + "\nPRODUCT_QUANTITY_UPDATE_SERVICE = 5"
                    + "\nUSERNAME_PASSWORD_VALID_SERVICE = 6"
                    + "\nCAN_USER_CANCEL_ORDER_SERVICE = 7"
                    + "\nEnter value: ");
            int ms;
            try {
                ms = scanner.nextInt();
            } catch (InputMismatchException err) {
                continue;
            }
            if (ms < 0 && ms > 7) continue;

            ServiceInfoModel service;
            DataOutputStream printer;
            DataInputStream reader;
            int id;
            String msg;

            switch (ms) {
                case 1:
                    // Get microservice socket
                    service = requestService(ServiceInfoModel.PRODUCT_INFO_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    // Do the stuff to use the microservice
                    System.out.print("Enter a valid product ID: ");
                    id = new Scanner(System.in).nextInt();

                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeInt(id);
                    printer.flush();

                    System.out.println("Waiting for response...");

                    reader = new DataInputStream(socket.getInputStream());
                    msg = reader.readUTF();
                    ProductModel product = new Gson().fromJson(msg, ProductModel.class);

                    if (product == null) {
                        System.out.println("Invalid ID");
                    } else {
                        System.out.println("\nID: " + product.productID +
                                "\nProduct Name: " + product.productName +
                                "\nSeller Name: " + product.sellerName +
                                "\nPrice: " + product.price +
                                "\nQuantity: " + product.quantity
                        );
                    }
                    break;
                case 2:

                    service = requestService(ServiceInfoModel.ORDER_INFO_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    // Do the stuff to use the microservice
                    System.out.print("Enter a valid order ID: ");
                    id = new Scanner(System.in).nextInt();

                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeInt(id);
                    printer.flush();

                    System.out.println("Waiting for response...");

                    reader = new DataInputStream(socket.getInputStream());
                    msg = reader.readUTF();
                    OrderModel order = new Gson().fromJson(msg, OrderModel.class);

                    if (order == null) {
                        System.out.println("Invalid ID");
                    } else {
                        System.out.println("\nID: " + order.orderID +
                                "\nProduct Name: " + order.productName +
                                "\nBuyer Name: " + order.buyerName +
                                "\nSeller Name: " + order.sellerName +
                                "\nPrice: " + order.price +
                                "\nQuantity: " + order.quantity +
                                "\nShipping Address: " + order.shippingDestinationAddress
                        );
                    }
                    break;
                case 3:
                    service = requestService(ServiceInfoModel.USER_INFO_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    // Do the stuff to use the microservice
                    System.out.print("Enter a valid user ID: ");
                    id = new Scanner(System.in).nextInt();

                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeInt(id);
                    printer.flush();

                    System.out.println("Waiting for response...");

                    reader = new DataInputStream(socket.getInputStream());
                    msg = reader.readUTF();
                    UserModel user = new Gson().fromJson(msg, UserModel.class);

                    if (user == null) {
                        System.out.println("Invalid ID");
                    } else {
                        System.out.println("\nID: " + user.userID +
                                "\nUsername: " + user.username +
                                "\nPassword: " + user.password
                        );
                    }
                    break;
                case 4:
                    service = requestService(ServiceInfoModel.PRODUCT_PRICE_UPDATE_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    PriceIDModel priceID = new PriceIDModel();

                    System.out.print("Enter a valid product ID: ");
                    priceID.productID = new Scanner(System.in).nextInt();

                    System.out.print("Enter a valid price: ");
                    priceID.price = new Scanner(System.in).nextDouble();

                    String req = new Gson().toJson(priceID);
                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeUTF(req);
                    break;
                case 5:
                    service = requestService(ServiceInfoModel.PRODUCT_QUANTITY_UPDATE_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    QuantityIDModel quantityID = new QuantityIDModel();

                    System.out.print("Enter a valid product ID: ");
                    quantityID.productID = new Scanner(System.in).nextInt();

                    System.out.print("Enter a valid quantity: ");
                    quantityID.quantity = new Scanner(System.in).nextInt();

                    req = new Gson().toJson(quantityID);
                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeUTF(req);
                    break;
                case 6:
                    service = requestService(ServiceInfoModel.USERNAME_PASSWORD_VALID_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    UsernamePasswordModel usernamePassword = new UsernamePasswordModel();

                    System.out.print("Enter a valid username: ");
                    usernamePassword.username = new Scanner(System.in).nextLine();

                    System.out.print("Enter user's password: ");
                    usernamePassword.password = new Scanner(System.in).nextLine();

                    req = new Gson().toJson(usernamePassword);
                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeUTF(req);
                    printer.flush();

                    System.out.println("Waiting for response...");

                    reader = new DataInputStream(socket.getInputStream());
                    msg = reader.readUTF();
                    Boolean validLogin = new Gson().fromJson(msg, Boolean.class);

                    if (validLogin)
                        System.out.println("Correct login credentials provided!");
                    else
                        System.out.println("Incorrect login credentials provided.");

                    break;
                case 7:
                    service = requestService(ServiceInfoModel.CAN_USER_CANCEL_ORDER_SERVICE, socket);
                    socket.close();
                    if (service == null) return;
                    socket = new Socket("localhost", service.serviceHostPort);

                    UsernameOrderModel usernameOrder = new UsernameOrderModel();

                    System.out.print("Enter a valid username: ");
                    usernameOrder.username = new Scanner(System.in).nextLine();

                    System.out.print("Enter a valid order ID: ");
                    usernameOrder.orderID = new Scanner(System.in).nextInt();

                    req = new Gson().toJson(usernameOrder);
                    printer = new DataOutputStream(socket.getOutputStream());
                    printer.writeUTF(req);
                    printer.flush();

                    System.out.println("Waiting for response...");

                    reader = new DataInputStream(socket.getInputStream());
                    msg = reader.readUTF();
                    validLogin = new Gson().fromJson(msg, Boolean.class);

                    if (validLogin)
                        System.out.println("User can cancel order!");
                    else
                        System.out.println("User cannot cancel order.");

                    break;
            }
        }
    }

    private static ServiceInfoModel requestService(int id, Socket socket) throws IOException {

        ServiceMessageModel req = new ServiceMessageModel();

        req.code = ServiceMessageModel.SERVICE_DISCOVER_REQUEST;
        req.data = String.valueOf(id);

        DataOutputStream printer = new DataOutputStream(socket.getOutputStream());
        DataInputStream reader = new DataInputStream(socket.getInputStream());

        printer.writeUTF(new Gson().toJson(req));
        printer.flush();

        String msg = reader.readUTF();

        printer.close();
        reader.close();

        ServiceMessageModel res = new Gson().fromJson(msg, ServiceMessageModel.class);
        if (res.code == ServiceMessageModel.SERVICE_DISCOVER_OK) {
//            System.out.println(res.data);
        } else {
            System.out.println("Service not found");
            return null;
        }

        ServiceInfoModel info = new Gson().fromJson(res.data, ServiceInfoModel.class);
        return info;
    }
}
