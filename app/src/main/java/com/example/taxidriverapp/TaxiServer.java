package com.example.taxidriverapp;

import com.example.taxidriverapp.activity.LoginActivity;
import com.example.taxidriverapp.models.Area;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import static com.example.taxidriverapp.activity.LoginActivity.myId;
import static com.example.taxidriverapp.activity.LoginActivity.myLogin;

public class TaxiServer {
    private static final String SERVER_IP = "192.168.1.101";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_LOGOUT = 1;
    private static final int REQUEST_GET_AREAS = 5;
    private static final int REQUEST_COUNT_DRIVERS = 6;
    private static TaxiServer taxiServer = new TaxiServer();
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
            input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "windows-1251"));
            output.println(REQUEST_LOGIN + ":" + userName + ":" + password);
            String[] massage = input.readLine().split(":");
            switch (massage[0]) {
                case "0":
                    return 0;
                case "1":
                    myId = Integer.valueOf(massage[1]);
                    myLogin = userName;
                    return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int logout() {
        output.println(REQUEST_LOGOUT + ":" + LoginActivity.myId);
        return -1;
    }

    public String getAreas() {
        output.println(REQUEST_GET_AREAS);
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String countDriversInArea(int idArea) {
        output.println(REQUEST_COUNT_DRIVERS + ":" + idArea);
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Data {
        public static ArrayList<Area> areas = new ArrayList<>();
    }
}
