package com.example.taxidriverapp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TaxiServer {
    private static final String SERVER_IP = "192.168.1.101";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_ORDERS = 1;
    private static final int REQUEST_TAKE_ORDER = 2;
    private static final int REQUEST_FINISH_ORDER = 3;
    private static final TaxiServer taxiServer = new TaxiServer();
    private static final ObjectMapper mapper = new ObjectMapper();
    private PrintStream output;
    private BufferedReader input;

    private TaxiServer() {

    }

    public static TaxiServer getInstance() {
        return taxiServer;
    }

    public int login(String userName, String password) {
        try {
            Socket socket = new Socket(SERVER_IP, 9999);
            output = new PrintStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output.println(REQUEST_LOGIN + ":" + userName + ":" + password);
            return Integer.valueOf(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void getOrders() {
        try {
            output.println(REQUEST_ORDERS);
            Data.getInstance().orders.addAll((ArrayList<Order>)
                    mapper.readValue(input.readLine(),
                            mapper.getTypeFactory()
                                    .constructCollectionType(List.class, Order.class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int takeOrder(int idOrder) {
        try {
            output.println(REQUEST_TAKE_ORDER + ":" + idOrder);
            return Integer.valueOf(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void finishOrder(int idOrder) {
        output.println(REQUEST_FINISH_ORDER + ":" + idOrder);
    }

    public static class Data {
        private static final Data data = new Data();
        public ArrayList<Order> orders = new ArrayList<>();
        public Order order;

        private Data() {
        }

        public static Data getInstance() {
            return data;
        }
    }
}
