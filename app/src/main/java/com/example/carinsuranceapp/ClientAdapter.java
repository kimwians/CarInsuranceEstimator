package com.example.carinsuranceapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ClientAdapter extends ArrayAdapter<Client> {

    private ArrayList<Client> items;
    private Context adapterContext;

    public ClientAdapter(Context context, ArrayList<Client> items) {
        super(context, R.layout.list_item, items);
        adapterContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        try {
            Client client = items.get(position);

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }

            TextView monthlyRate = v.findViewById(R.id.txtMonthlyRate);
            TextView clientId = v.findViewById(R.id.txtClientID);
            TextView clientName = v.findViewById(R.id.txtClientName);
            Button b = v.findViewById(R.id.buttonDeleteClient);
            monthlyRate.setText("$" + client.getMonthlyRate() + ".00");
            clientId.setText("ID#: " + Integer.toString(client.getClientID()));
            clientName.setText("Name: " + client.getClientName());
            b.setVisibility(View.INVISIBLE);
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            Log.w(ClientAdapter.class.getName(), "Failed to inflate list item layout");
        }
        return v;
    }

    public void showDelete(final int position, final View convertView,
                           final Context context, final Client client) {
        View v = convertView;
        final Button b = v.findViewById(R.id.buttonDeleteClient);
        if (b.getVisibility() == View.INVISIBLE) {
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDelete(position, convertView, context);
                    items.remove(client);
                    deleteOption(client.getClientID(), context);
                }
            });
        }
        else {
            hideDelete(position, convertView, context);
        }
    }

    private void deleteOption(int clientToDelete, Context context) {
        ClientDataSource ds = new ClientDataSource(context);
        try {
            ds.open();
            ds.deleteClient(clientToDelete);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(adapterContext, "Delete Client Failed", Toast.LENGTH_LONG).show();
        }
        this.notifyDataSetChanged();
    }

    public void hideDelete(int position, View convertView, Context context) {
        View v = convertView;
        final Button b = v.findViewById(R.id.buttonDeleteClient);
        b.setVisibility(View.INVISIBLE);
        b.setOnClickListener(null);
    }
}
