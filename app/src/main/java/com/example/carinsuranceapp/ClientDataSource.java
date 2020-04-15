package com.example.carinsuranceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

public class ClientDataSource {

    private SQLiteDatabase database;
    private ClientDBHelper dbHelper;

    public ClientDataSource(Context context) {
        dbHelper = new ClientDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertClient(Client c) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("clientname", c.getClientName());
            initialValues.put("age", c.getAge());
            initialValues.put("gender", c.getGender());
            initialValues.put("marriagestatus", c.getMarriageStatus());
            initialValues.put("streetaddress", c.getAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZip());
            initialValues.put("carvalue", c.getValue());
            initialValues.put("monthlyrate", c.getMonthlyRate());

            didSucceed = database.insert("client", null, initialValues) > 0;
        }
        catch (Exception e) {
            // didSucceed will return false if there is an exception
            Log.w(ClientDataSource.class.getName(), "Failed to insert client");
        }
        return didSucceed;
    }

    public boolean updateClient(Client c) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) c.getClientID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("clientname", c.getClientName());
            updateValues.put("age", c.getAge());
            updateValues.put("gender", c.getGender());
            updateValues.put("marriagestatus", c.getMarriageStatus());
            updateValues.put("streetaddress", c.getAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZip());
            updateValues.put("carvalue", c.getValue());
            updateValues.put("monthlyrate", c.getMonthlyRate());

            didSucceed = database.update("client", updateValues, "_id=" + rowId, null) > 0;
        }
        catch (Exception e) {
            // didSucceed will return false if there is an exception
            Log.w(ClientDataSource.class.getName(), "Failed to update client");
        }
        return didSucceed;
    }

    public boolean deleteClient(int clientid) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("client", "_id=" + clientid, null) > 0;
        }
        catch (Exception e) {
            // didDelete will return false if there is an exception
            Log.w(ClientDataSource.class.getName(), "Failed to delete client");
        }
        return didDelete;
    }

    public int getLastClientId() {
        int lastId = -1;
        try {
            String query = "select MAX(_id) from client";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
            Log.w(ClientDataSource.class.getName(), "Could not retrieve ID. ID = " + lastId);
        }
        return lastId;
    }

    public ArrayList<Client> getClients() {
        ArrayList<Client> clients = new ArrayList<>();
        try {
            String query = "SELECT * FROM client ORDER BY monthlyrate";
            Cursor cursor = database.rawQuery(query, null);

            Client newClient;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newClient = new Client();
                newClient.setClientID(cursor.getInt(0));
                newClient.setClientName(cursor.getString(1));
                newClient.setAge(cursor.getString(2));
                newClient.setGender(cursor.getString(3));
                newClient.setMarriageStatus(cursor.getString(4));
                newClient.setAddress(cursor.getString(5));
                newClient.setCity(cursor.getString(6));
                newClient.setState(cursor.getString(7));
                newClient.setZip(cursor.getString(8));
                newClient.setValue(cursor.getString(9));
                newClient.setMonthlyRate(cursor.getString(10));
                clients.add(newClient);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            clients = new ArrayList<Client>();
            Log.w(ClientDataSource.class.getName(), "Failed to get client list");
        }
        return clients;
    }

    public Client getSpecificClient(int clientId) {
        Client client = new Client();
        String query = "SELECT * FROM client WHERE _id = " + clientId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            client.setClientID(cursor.getInt(0));
            client.setClientName(cursor.getString(1));
            client.setAge(cursor.getString(2));
            client.setGender(cursor.getString(3));
            client.setMarriageStatus(cursor.getString(4));
            client.setAddress(cursor.getString(5));
            client.setCity(cursor.getString(6));
            client.setState(cursor.getString(7));
            client.setZip(cursor.getString(8));
            client.setValue(cursor.getString(9));
            client.setMonthlyRate(cursor.getString(10));

            cursor.close();
        }
        return client;
    }

    // find 3 nearest neighbors - 3 smallest distances
    public int[] findNearestNeighbors(int[] arr1) {
        int[] monthlyRates = new int[3];
        double min = 1000.0;
        double min2 = 1000.0;
        double min3 = 1000.0;

        try {
            String query = "SELECT * FROM client";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Client client = new Client();
                client.setClientID(cursor.getInt(0));
                client.setClientName(cursor.getString(1));
                client.setAge(cursor.getString(2));
                client.setGender(cursor.getString(3));
                client.setMarriageStatus(cursor.getString(4));
                client.setAddress(cursor.getString(5));
                client.setCity(cursor.getString(6));
                client.setState(cursor.getString(7));
                client.setZip(cursor.getString(8));
                client.setValue(cursor.getString(9));
                client.setMonthlyRate(cursor.getString(10));

                int[] arr2 = MainActivity.getArray(client);
                double distance = MainActivity.findDistance(arr1, arr2);

                if (distance < min) {
                    min3 = min2;
                    min2 = min;
                    min = distance;

                    monthlyRates[2] = monthlyRates[1];
                    monthlyRates[1] = monthlyRates[0];
                    monthlyRates[0] = Integer.parseInt(client.getMonthlyRate());
                }
                else if (distance < min2) {
                    min3 = min2;
                    min2 = distance;

                    monthlyRates[2] = monthlyRates[1];
                    monthlyRates[1] = Integer.parseInt(client.getMonthlyRate());
                }
                else if (distance < min3) {
                    min3 = distance;
                    monthlyRates[2] = Integer.parseInt(client.getMonthlyRate());
                }

                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            Log.w(ClientDataSource.class.getName(), "Failed to get clients");
        }
        return monthlyRates;
    }
}
